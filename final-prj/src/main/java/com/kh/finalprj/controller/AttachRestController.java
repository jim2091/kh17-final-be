package com.kh.finalprj.controller;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment; // 스프링 환경 인터페이스
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kh.finalprj.configuration.StorageProperties;
import com.kh.finalprj.dao.AttachDao;
import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.service.AttachService;
import com.kh.finalprj.vo.attach.AttachInfoVO;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Slf4j
@RestController
@RequestMapping("/api/attach")
public class AttachRestController {
	
	@Autowired
	private AttachService attachService;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private AttachDao attachDao;
	
	@Autowired
	private S3Presigner s3Presigner;
	
	@Autowired
	private StorageProperties storageProperties;
	
	// 1. 파일 업로드 (프로젝트 파일함용)
	@PostMapping("/upload")
	public int upload(
			@RequestParam int projectNo,
			@RequestParam MultipartFile attach,
			@RequestParam(required = false) String uploader,
			@RequestParam(required = false) String source
	) throws IllegalStateException, IOException {
		return attachService.save(projectNo, attach, uploader, source);
	}
	
	// 2. 파일 다운로드 (클라우드 모드면 S3 Presigned URL로 리다이렉트, 로컬이면 서버에서 직접 전송)
	@GetMapping("/{attachNo}")
	public ResponseEntity<?> download(
			@PathVariable int attachNo
	) throws IOException {
		// 클라우드 프로필일 경우 S3 Presigned URL 리다이렉트 경로로 토스
		if(environment.matchesProfiles("cloud")) {
			return ResponseEntity.status(302)
						.location(URI.create("./p/" + attachNo))
					.build();
		}
		
		// 로컬 프로필일 경우 서버가 직접 파일 데이터를 읽어서 전송
		AttachInfoVO vo = attachService.load(attachNo);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_ENCODING, "UTF-8")
				.header(HttpHeaders.CONTENT_TYPE, vo.getAttachDto().getAttachType())
				.contentLength(vo.getAttachDto().getAttachSize())
				.header(
						HttpHeaders.CONTENT_DISPOSITION, 
						ContentDisposition
								.attachment()
								.filename(
										vo.getAttachDto().getAttachName(),
										StandardCharsets.UTF_8
								)
								.build().toString()
				)
				.body(vo.getResource());
	}
	
	// 3. AWS S3 Presigned URL 발급 및 리다이렉트 (클라우드 다운로드 핵심)
	@GetMapping("/p/{attachNo}")
	public ResponseEntity<?> presigned(@PathVariable int attachNo) {
		AttachDto attachDto = attachDao.selectOne(attachNo);
		if(attachDto == null) throw new TargetNotfoundException();
		
		String objectKey = storageProperties.getAwsRoot() + "/" + attachNo;
		
		GetObjectRequest request = GetObjectRequest.builder()
					.bucket(storageProperties.getAwsBucket())
					.key(objectKey)
					.responseContentDisposition(
							ContentDisposition
									.attachment()
									.filename(
											attachDto.getAttachName(),
											StandardCharsets.UTF_8
									)
									.build().toString()
					)
				.build();
		
		GetObjectPresignRequest presignRequest =
				GetObjectPresignRequest.builder()
						.signatureDuration(Duration.ofMinutes(storageProperties.getPresignedLimit()))
						.getObjectRequest(request)
				.build();
		
		String url = s3Presigner.presignGetObject(presignRequest).url().toString();
		
		log.debug("presigned url = {}", url);
		
		return ResponseEntity
					.status(302)
					.location(URI.create(url))
				.build();
	}
	
	// 4. 파일 삭제
	@DeleteMapping("/{attachNo}")
	public void delete(@PathVariable int attachNo) {
		attachService.delete(attachNo);
	}
	
	// 5. 프로젝트별 파일 목록 조회 및 검색
	@GetMapping("/list/{projectNo}")
	public List<AttachDto> list(
			@PathVariable int projectNo,
			@RequestParam(required = false) String keyword
	) {
		if(keyword == null || keyword.trim().equals("")) {
			return attachService.list(projectNo);
		} else {
			return attachService.list(projectNo, keyword);
		}
	}
}