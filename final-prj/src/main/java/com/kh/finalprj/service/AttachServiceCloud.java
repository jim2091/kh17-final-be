package com.kh.finalprj.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.finalprj.configuration.StorageProperties;
import com.kh.finalprj.dao.AttachDao;
import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.vo.attach.AttachInfoVO;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
@Slf4j
@Service
@Profile("cloud")
public class AttachServiceCloud implements AttachService{
	@Autowired
	private AttachDao attachDao;
	
	@Autowired
	private S3Client s3Client;
	
	@Autowired
	private StorageProperties storageProperties;
	
	//1. 일반 파일 저장
	@Transactional
	@Override
	public int save(MultipartFile attach) throws IllegalStateException, IOException {
		return save(0, attach, null, null);
	}
	
	//2.  프로젝트 파일함용 파일 저장
	@Transactional
	@Override
	public int save(
	        int projectNo,
	        MultipartFile attach,
	        String uploader,
	        String source
	) throws IllegalStateException, IOException {

	    if(attach == null || attach.isEmpty()) {
	        return 0;
	    }

	    // 파일 업로더가 없으면 업로드 자체를 막음
	    if(uploader == null || uploader.trim().isEmpty()) {
	        throw new IllegalStateException("파일 업로더 정보가 없습니다.");
	    }

	    // source 기본값
	    if(source == null || source.trim().isEmpty()) {
	        source = "파일함";
	    }

	    int attachNo = attachDao.sequence();

	    // DB 저장
	    attachDao.insert(
	        AttachDto.builder()
	            .attachNo(attachNo)
	            .projectNo(projectNo)
	            .attachName(attach.getOriginalFilename())
	            .attachType(attach.getContentType())
	            .attachSize(attach.getSize())
	            .attachUploader(uploader)
	            .attachSource(source)
	            .build()
	    );

	    // AWS S3 저장
	    String objectKey = storageProperties.getAwsRoot() + "/" + attachNo;

	    PutObjectRequest request = PutObjectRequest.builder()
	            .bucket(storageProperties.getAwsBucket())
	            .key(objectKey)
	            .contentType(attach.getContentType())
	            .build();

	    PutObjectResponse response =
	            s3Client.putObject(
	                request,
	                RequestBody.fromBytes(attach.getBytes())
	            );

	    log.debug("<AWS S3 프로젝트 파일 업로드 완료>");
	    log.debug("object key = {}", objectKey);
	    log.debug("ETag = {}", response.eTag());

	    return attachNo;
	}
	
	//3. 파일 삭제 (DB + AWS S3)
	@Transactional
	@Override
	public void delete(Integer attachNo) {
		if(attachNo == null)return;
		
		//DB 정보 삭제
		attachDao.delete(attachNo);
		
		//AWS S3 파일 삭제 요청
		String objectKey = storageProperties.getAwsRoot() + "/" + attachNo;
		DeleteObjectRequest request = DeleteObjectRequest.builder()
					.bucket(storageProperties.getAwsBucket())
					.key(objectKey)
				.build();
		
		DeleteObjectResponse response = s3Client.deleteObject(request);
		
		log.debug("<AWS 파일 삭제 완료>");
		log.debug("HTTP status = {}", response.sdkHttpResponse().statusCode());
	}
	
	//4. 파일 로드 (다운로드용)
	@Override
	public AttachInfoVO load(int attachNo) throws IOException {
		AttachDto attachDto = attachDao.selectOne(attachNo);
		if(attachDto == null) throw new TargetNotfoundException();
		
		String objectKey = storageProperties.getAwsRoot() + "/" + attachNo;
		
		GetObjectRequest request = GetObjectRequest.builder()
					.bucket(storageProperties.getAwsBucket())
					.key(objectKey)
				.build();
		
		ResponseInputStream<GetObjectResponse> stream = s3Client.getObject(request);
		GetObjectResponse response = stream.response();
		
		log.debug("Content-Type = {}", response.contentType());
		log.debug("Content-Length = {}", response.contentLength());
		log.debug("ETag = {}", response.eTag());
		
		byte[] data = stream.readAllBytes();
		Resource resource = new ByteArrayResource(data);
		
		stream.close();
		
		return AttachInfoVO.builder()
					.attachDto(attachDto)
					.resource(resource)
				.build();
	}

	//5. 프로젝트별 파일 목록 조회
	@Override
	public List<AttachDto> list(int projectNo) {
		return attachDao.selectListByProject(projectNo);
	}

	//6. 프로젝트별 파일 검색 조회
	@Override
	public List<AttachDto> list(int projectNo, String keyword) {
		return attachDao.selectListByProjectAndKeyword(projectNo, keyword);
	}
	
}
