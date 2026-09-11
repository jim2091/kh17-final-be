package com.kh.finalprj.controller;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import com.kh.finalprj.dao.ProjectMemberDao;
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
    private ProjectMemberDao projectMemberDao;

    @Autowired
    private S3Presigner s3Presigner;

    @Autowired
    private StorageProperties storageProperties;


    // =========================================================
    // 1. 파일 업로드
    // =========================================================

    @PostMapping("/upload")
    public int upload(
            @RequestParam int projectNo,
            @RequestParam MultipartFile attach,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) Integer sourceNo,
            Authentication authentication
    ) throws IllegalStateException, IOException {

        String uploader = null;

        if (authentication != null) {
            uploader = authentication.getName();
        }

        log.debug("projectNo = {}", projectNo);
        log.debug("uploader = {}", uploader);
        log.debug("source = {}", source);
        log.debug("sourceNo = {}", sourceNo);

        if (uploader == null || uploader.trim().isEmpty()) {
            throw new IllegalStateException("로그인 사용자 정보가 없습니다.");
        }

        return attachService.save(
                projectNo,
                attach,
                uploader,
                source
        );
    }


    // =========================================================
    // 2. 파일 다운로드 및 인라인 조회 (미스매치 원천 해결)
    // =========================================================

    @GetMapping({"/{attachNo}", "/download/{attachNo}"})
    public ResponseEntity<Resource> download(
            @PathVariable int attachNo
    ) throws IOException {

        if (environment.matchesProfiles("cloud")) {
            return ResponseEntity
                    .status(302)
                    .location(URI.create("./p/" + attachNo))
                    .build();
        }

        AttachInfoVO vo = attachService.load(attachNo);
        if (vo == null || vo.getResource() == null) {
            return ResponseEntity.notFound().build();
        }

        AttachDto attachDto = vo.getAttachDto();
        Resource resource = vo.getResource();

        // [핵심 해결] DB에 기록된 attachSize 대신 실제 Resource의 바이트 길이 사용
        long realContentLength = -1;
        try {
            realContentLength = resource.contentLength();
        } catch (Exception e) {
            realContentLength = attachDto != null ? attachDto.getAttachSize() : -1;
        }

        // 미디어 타입 파싱 방어
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (attachDto != null && attachDto.getAttachType() != null) {
            try {
                mediaType = MediaType.parseMediaType(attachDto.getAttachType());
            } catch (Exception ignored) {}
        }

        String fileName = (attachDto != null && attachDto.getAttachName() != null)
                ? attachDto.getAttachName()
                : "file_" + attachNo;

        // Content-Disposition: 브라우저 인라인 미리보기와 한글 파일명 다운로드 호환
        String disposition = ContentDisposition.inline()
                .filename(fileName, StandardCharsets.UTF_8)
                .build()
                .toString();

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition);

        // 실제 계산된 바이트 길이가 0 이상일 때만 헤더 세팅 (길이 불일치 오류 방지)
        if (realContentLength > 0) {
            responseBuilder.contentLength(realContentLength);
        }

        return responseBuilder.body(resource);
    }


    // =========================================================
    // 3. AWS S3 Presigned URL
    // =========================================================

    @GetMapping("/p/{attachNo}")
    public ResponseEntity<?> presigned(
            @PathVariable int attachNo
    ) {

        AttachDto attachDto = attachDao.selectOne(attachNo);
        if (attachDto == null) {
            throw new TargetNotfoundException();
        }

        String objectKey = storageProperties.getAwsRoot() + "/" + attachNo;

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(storageProperties.getAwsBucket())
                .key(objectKey)
                .responseContentDisposition(
                        ContentDisposition.attachment()
                                .filename(attachDto.getAttachName(), StandardCharsets.UTF_8)
                                .build()
                                .toString()
                )
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(storageProperties.getPresignedLimit()))
                .getObjectRequest(request)
                .build();

        String url = s3Presigner.presignGetObject(presignRequest).url().toString();

        return ResponseEntity
                .status(302)
                .location(URI.create(url))
                .build();
    }


    // =========================================================
    // 4. 파일 삭제
    // =========================================================

    @DeleteMapping("/{attachNo}")
    public void delete(
            @PathVariable int attachNo,
            Authentication authentication
    ) {

        if (authentication == null) {
            throw new IllegalStateException("로그인 사용자 정보가 없습니다.");
        }

        String uploader = authentication.getName();

        log.debug("삭제 요청 파일 번호 = {}", attachNo);
        log.debug("삭제 요청 사용자 = {}", uploader);

        attachService.delete(attachNo, uploader);
    }


    // =========================================================
    // 5. 프로젝트별 파일 목록 조회
    // =========================================================

    @GetMapping("/list/{projectNo}")
    public ResponseEntity<?> list(
            @PathVariable int projectNo,
            @RequestParam(required = false) String keyword,
            Authentication authentication
    ) {

        if (authentication == null) {
            throw new IllegalStateException("로그인 사용자 정보가 없습니다.");
        }

        String loginUser = authentication.getName();
        List<AttachDto> files;

        if (keyword == null || keyword.trim().isEmpty()) {
            files = attachService.list(projectNo);
        } else {
            files = attachService.list(projectNo, keyword);
        }

        // 현재 사용자의 프로젝트 역할 조회
        String loginRole = null;
        try {
            int empNo = Integer.parseInt(loginUser);
            loginRole = projectMemberDao.selectRole(projectNo, empNo);
        } catch (NumberFormatException e) {
            log.warn("로그인 사용자 번호 변환 실패: {}", loginUser);
        }

        log.debug("프로젝트 파일 목록 조회");
        log.debug("projectNo = {}", projectNo);
        log.debug("현재 로그인 사용자 = {}", loginUser);
        log.debug("현재 사용자 역할 = {}", loginRole);

        return ResponseEntity.ok(
                Map.of(
                        "files", files,
                        "loginUser", loginUser,
                        "loginRole", loginRole == null ? "" : loginRole
                )
        );
    }
}