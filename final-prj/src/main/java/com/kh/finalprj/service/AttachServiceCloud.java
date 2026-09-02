package com.kh.finalprj.service;

import java.io.File;
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
import com.kh.finalprj.dao.ProjectFileDao;
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
public class AttachServiceCloud implements AttachService {

    @Autowired
    private AttachDao attachDao;

    @Autowired
    private ProjectFileDao projectFileDao;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private StorageProperties storageProperties;


    // =========================================================
    // 1. 파일 저장
    // =========================================================

    @Transactional
    @Override
    public int save(
            int projectNo,
            MultipartFile attach,
            String uploader,
            String source
    ) throws IllegalStateException, IOException {

        if (attach == null || attach.isEmpty()) {
            return 0;
        }

        if (uploader == null ||
                uploader.trim().isEmpty()) {

            throw new IllegalStateException(
                    "파일 업로더 정보가 없습니다."
            );
        }

        if (source == null ||
                source.trim().isEmpty()) {

            source = "파일함";
        }


        // 파일 번호 생성
        int attachNo = attachDao.sequence();


        // 파일 정보 생성
        AttachDto dto = AttachDto.builder()
                .attachNo(attachNo)
                .attachName(attach.getOriginalFilename())
                .attachType(attach.getContentType())
                .attachSize(attach.getSize())
                .attachUploader(uploader)
                .attachSource(source)
                .build();


        // 1. attach 테이블 저장
        attachDao.insert(dto);


        // 2. 프로젝트와 파일 연결
        projectFileDao.insert(projectNo, attachNo);


        // 3. AWS S3 저장
        String objectKey =
                storageProperties.getAwsRoot()
                + "/"
                + attachNo;


        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucket(
                                storageProperties.getAwsBucket()
                        )
                        .key(objectKey)
                        .contentType(
                                attach.getContentType()
                        )
                        .build();


        PutObjectResponse response =
                s3Client.putObject(
                        request,
                        RequestBody.fromBytes(
                                attach.getBytes()
                        )
                );


        log.debug("<AWS S3 파일 업로드 완료>");
        log.debug("projectNo = {}", projectNo);
        log.debug("attachNo = {}", attachNo);
        log.debug("object key = {}", objectKey);
        log.debug("ETag = {}", response.eTag());


        return attachNo;
    }


    // =========================================================
    // 2. 파일 삭제
    // =========================================================

    @Transactional
    @Override
    public void delete(
            Integer attachNo,
            String uploader
    ) {

        if (attachNo == null) {
            return;
        }

        AttachDto attachDto =
                attachDao.selectOne(attachNo);

        if (attachDto == null) {
            throw new TargetNotfoundException();
        }

        if (uploader == null ||
                uploader.trim().isEmpty()) {

            throw new IllegalStateException(
                    "로그인 사용자 정보가 없습니다."
            );
        }

        if (!uploader.equals(
                attachDto.getAttachUploader()
        )) {

            throw new IllegalStateException(
                    "본인이 업로드한 파일만 삭제할 수 있습니다."
            );
        }


        // attach 삭제
        // project_file은 FK ON DELETE CASCADE라서
        // 연결 관계도 같이 삭제됩니다.
        attachDao.delete(attachNo);


        // S3 삭제
        String objectKey =
                storageProperties.getAwsRoot()
                + "/"
                + attachNo;


        DeleteObjectRequest request =
                DeleteObjectRequest.builder()
                        .bucket(
                                storageProperties.getAwsBucket()
                        )
                        .key(objectKey)
                        .build();


        DeleteObjectResponse response =
                s3Client.deleteObject(request);


        log.debug("<AWS 파일 삭제 완료>");
        log.debug(
                "HTTP status = {}",
                response.sdkHttpResponse().statusCode()
        );
    }


    // =========================================================
    // 3. 파일 로드
    // =========================================================

    @Override
    public AttachInfoVO load(
            int attachNo
    ) throws IOException {

        AttachDto attachDto =
                attachDao.selectOne(attachNo);

        if (attachDto == null) {
            throw new TargetNotfoundException();
        }


        String objectKey =
                storageProperties.getAwsRoot()
                + "/"
                + attachNo;


        GetObjectRequest request =
                GetObjectRequest.builder()
                        .bucket(
                                storageProperties.getAwsBucket()
                        )
                        .key(objectKey)
                        .build();


        ResponseInputStream<GetObjectResponse> stream =
                s3Client.getObject(request);


        GetObjectResponse response =
                stream.response();


        log.debug(
                "Content-Type = {}",
                response.contentType()
        );

        log.debug(
                "Content-Length = {}",
                response.contentLength()
        );

        log.debug(
                "ETag = {}",
                response.eTag()
        );


        byte[] data =
                stream.readAllBytes();


        Resource resource =
                new ByteArrayResource(data);


        stream.close();


        return AttachInfoVO.builder()
                .attachDto(attachDto)
                .resource(resource)
                .build();
    }


    // =========================================================
    // 4. 프로젝트 파일 목록
    // =========================================================

    @Override
    public List<AttachDto> list(
            int projectNo
    ) {

        return attachDao.selectListByProject(
                projectNo
        );
    }


    // =========================================================
    // 5. 프로젝트 파일 검색
    // =========================================================

    @Override
    public List<AttachDto> list(
            int projectNo,
            String keyword
    ) {

        return attachDao.selectListByProjectAndKeyword(
                projectNo,
                keyword
        );
    }
    
    
  //회원 프로필 저장용도 
    @Transactional
	@Override
	public int save(MultipartFile attach) throws IllegalStateException, IOException{
			int attachNo = attachDao.sequence();
			attachDao.insert(
						AttachDto.builder()
							.attachNo(attachNo)
							.attachName(attach.getOriginalFilename())
							.attachType(attach.getContentType())
							.attachSize(attach.getSize())
						.build()
					);//db저장 
			
			
			
			
			//업로드된 파일을 저장하는 코드
			File dir = storageProperties.getLocalRoot();
			dir.mkdirs();
			File target = new File(dir, String.valueOf(attachNo));
			attach.transferTo(target);//물리저장
			return attachNo;
		}
}