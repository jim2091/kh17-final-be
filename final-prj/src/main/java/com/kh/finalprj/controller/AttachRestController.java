package com.kh.finalprj.controller;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
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


    // ==================================================
    // 파일 업로드
    // ==================================================

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

        log.debug(
                "projectNo = {}",
                projectNo
        );

        log.debug(
                "uploader = {}",
                uploader
        );

        log.debug(
                "source = {}",
                source
        );

        log.debug(
                "sourceNo = {}",
                sourceNo
        );

        if (
                uploader == null
                || uploader.trim().isEmpty()
        ) {
            throw new IllegalStateException(
                    "로그인 사용자 정보가 없습니다."
            );
        }

        String projectStatus =
                attachDao.selectProjectStatus(
                        projectNo
                );

        log.debug(
                "업로드 프로젝트 상태 = {}",
                projectStatus
        );

        if (
                "closed".equalsIgnoreCase(
                        projectStatus
                )
        ) {
            throw new IllegalStateException(
                    "종료된 프로젝트에는 파일을 업로드할 수 없습니다."
            );
        }

        return attachService.save(
                projectNo,
                attach,
                uploader,
                source,
                sourceNo
        );
    }


    // ==================================================
    // 파일 / 프로필 이미지 조회
    //
    // 프론트는 둘 다
    //
    // /api/attach/{attachNo}
    //
    // 그대로 사용
    // ==================================================

    @GetMapping("/{attachNo}")
    public ResponseEntity<?> download(
            @PathVariable int attachNo
    ) throws IOException {

        log.debug(
                "첨부파일 조회 요청"
        );

        log.debug(
                "attachNo = {}",
                attachNo
        );

        log.debug(
                "현재 profile = {}",
                environment.getActiveProfiles()
        );


        // ==================================================
        // DB에서 파일 정보 조회
        // ==================================================

        AttachDto attachDto =
                attachDao.selectOne(
                        attachNo
                );

        if (attachDto == null) {

            log.warn(
                    "첨부파일 DB 정보를 찾을 수 없습니다. attachNo = {}",
                    attachNo
            );

            throw new TargetNotfoundException(
                    "첨부파일을 찾을 수 없습니다. attachNo = "
                            + attachNo
            );
        }


        log.debug(
                "attachName = {}",
                attachDto.getAttachName()
        );

        log.debug(
                "attachType = {}",
                attachDto.getAttachType()
        );

        log.debug(
                "attachSource = {}",
                attachDto.getAttachSource()
        );


        // ==================================================
        // Cloud
        // ==================================================

        if (
                environment.matchesProfiles(
                        "cloud"
                )
        ) {

            return createCloudResponse(
                    attachDto
            );
        }


        // ==================================================
        // Local
        // ==================================================

        AttachInfoVO vo =
                attachService.load(
                        attachNo
                );

        if (
                vo == null
                || vo.getAttachDto() == null
        ) {

            throw new TargetNotfoundException(
                    "첨부파일을 찾을 수 없습니다."
            );
        }


        boolean profile =
                isProfile(
                        attachDto
                );


        ContentDisposition disposition;


        if (profile) {

            disposition =
                    ContentDisposition
                            .inline()
                            .filename(
                                    vo.getAttachDto()
                                            .getAttachName(),
                                    StandardCharsets.UTF_8
                            )
                            .build();

        } else {

            disposition =
                    ContentDisposition
                            .attachment()
                            .filename(
                                    vo.getAttachDto()
                                            .getAttachName(),
                                    StandardCharsets.UTF_8
                            )
                            .build();
        }


        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_TYPE,
                        vo.getAttachDto()
                                .getAttachType()
                )
                .contentLength(
                        vo.getAttachDto()
                                .getAttachSize()
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        disposition.toString()
                )
                .body(
                        vo.getResource()
                );
    }


    // ==================================================
    // Cloud 응답
    //
    // PROFILE
    //     → 브라우저에서 이미지 표시
    //
    // 일반 파일
    //     → 파일 다운로드
    // ==================================================

    private ResponseEntity<?> createCloudResponse(
            AttachDto attachDto
    ) {

        int attachNo =
                attachDto.getAttachNo();

        String objectKey =
                storageProperties.getAwsRoot()
                        + "/"
                        + attachNo;


        boolean profile =
                isProfile(
                        attachDto
                );


        GetObjectRequest.Builder builder =
                GetObjectRequest
                        .builder()
                        .bucket(
                                storageProperties.getAwsBucket()
                        )
                        .key(objectKey);


        // ==================================================
        // 프로필 이미지
        // ==================================================

        if (profile) {

            builder
                    .responseContentType(
                            attachDto.getAttachType()
                    )
                    .responseContentDisposition(
                            "inline"
                    );

        }

        // ==================================================
        // 일반 파일
        // ==================================================

        else {

            builder
                    .responseContentType(
                            attachDto.getAttachType()
                    )
                    .responseContentDisposition(
                            ContentDisposition
                                    .attachment()
                                    .filename(
                                            attachDto
                                                    .getAttachName(),
                                            StandardCharsets.UTF_8
                                    )
                                    .build()
                                    .toString()
                    );
        }


        GetObjectRequest request =
                builder.build();


        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest
                        .builder()
                        .signatureDuration(
                                Duration.ofMinutes(
                                        storageProperties
                                                .getPresignedLimit()
                                )
                        )
                        .getObjectRequest(
                                request
                        )
                        .build();


        String url =
                s3Presigner
                        .presignGetObject(
                                presignRequest
                        )
                        .url()
                        .toString();


        log.debug(
                "Cloud 파일 URL 생성 완료. attachNo = {}",
                attachNo
        );

        log.debug(
                "프로필 여부 = {}",
                profile
        );


        return ResponseEntity
                .status(302)
                .location(
                        URI.create(url)
                )
                .build();
    }


    // ==================================================
    // 프로필 이미지 여부
    // ==================================================

    private boolean isProfile(
            AttachDto attachDto
    ) {

        if (attachDto == null) {
            return false;
        }

        String source =
                attachDto.getAttachSource();

        return source != null
                && "PROFILE".equalsIgnoreCase(
                        source.trim()
                );
    }


    // ==================================================
    // 기존 Cloud 다운로드 URL
    //
    // 다른 곳에서 /p/{attachNo}를 직접 사용할 수
    // 있으므로 유지
    //
    // 이 주소는 일반 파일 다운로드용
    // ==================================================

    @GetMapping("/p/{attachNo}")
    public ResponseEntity<?> presigned(
            @PathVariable int attachNo
    ) {

        log.debug(
                "Cloud 파일 다운로드 요청"
        );

        log.debug(
                "attachNo = {}",
                attachNo
        );


        AttachDto attachDto =
                attachDao.selectOne(
                        attachNo
                );

        if (attachDto == null) {

            throw new TargetNotfoundException(
                    "첨부파일을 찾을 수 없습니다. attachNo = "
                            + attachNo
            );
        }


        String objectKey =
                storageProperties.getAwsRoot()
                        + "/"
                        + attachNo;


        GetObjectRequest request =
                GetObjectRequest
                        .builder()
                        .bucket(
                                storageProperties.getAwsBucket()
                        )
                        .key(objectKey)
                        .responseContentType(
                                attachDto.getAttachType()
                        )
                        .responseContentDisposition(
                                ContentDisposition
                                        .attachment()
                                        .filename(
                                                attachDto
                                                        .getAttachName(),
                                                StandardCharsets.UTF_8
                                        )
                                        .build()
                                        .toString()
                        )
                        .build();


        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest
                        .builder()
                        .signatureDuration(
                                Duration.ofMinutes(
                                        storageProperties
                                                .getPresignedLimit()
                                )
                        )
                        .getObjectRequest(
                                request
                        )
                        .build();


        String url =
                s3Presigner
                        .presignGetObject(
                                presignRequest
                        )
                        .url()
                        .toString();


        return ResponseEntity
                .status(302)
                .location(
                        URI.create(url)
                )
                .build();
    }


    // ==================================================
    // 파일 삭제
    // ==================================================

    @DeleteMapping("/{attachNo}")
    public void delete(
            @PathVariable int attachNo,
            Authentication authentication
    ) {

        if (authentication == null) {

            throw new IllegalStateException(
                    "로그인 사용자 정보가 없습니다."
            );
        }

        String uploader =
                authentication.getName();

        log.debug(
                "삭제 요청 파일 번호 = {}",
                attachNo
        );

        log.debug(
                "삭제 요청 사용자 = {}",
                uploader
        );

        attachService.delete(
                attachNo,
                uploader
        );
    }


    // ==================================================
    // 프로젝트 파일 목록
    // + 검색
    // + 페이징
    // + 정렬
    // ==================================================

    @GetMapping("/list/{projectNo}")
    public ResponseEntity<?> list(
            @PathVariable int projectNo,
            @RequestParam(required = false)
            String keyword,
            @RequestParam(defaultValue = "name")
            String searchType,
            @RequestParam(defaultValue = "date-desc")
            String sortType,
            @RequestParam(defaultValue = "1")
            int page,
            @RequestParam(defaultValue = "12")
            int size,
            Authentication authentication
    ) {

        // ==================================================
        // 로그인 검사
        // ==================================================

        if (authentication == null) {

            throw new IllegalStateException(
                    "로그인 사용자 정보가 없습니다."
            );
        }

        String loginUser =
                authentication.getName();


        // ==================================================
        // page / size
        // ==================================================

        if (page < 1) {
            page = 1;
        }

        if (size < 1) {
            size = 12;
        }

        if (size > 100) {
            size = 100;
        }


        // ==================================================
        // 검색 타입
        // ==================================================

        if (
                !"name".equals(searchType)
                && !"source".equals(searchType)
                && !"uploader".equals(searchType)
                && !"type".equals(searchType)
        ) {
            searchType = "name";
        }


        // ==================================================
        // 정렬 타입
        // ==================================================

        if (
                !"date-desc".equals(sortType)
                && !"date-asc".equals(sortType)
                && !"name-asc".equals(sortType)
                && !"name-desc".equals(sortType)
                && !"size-desc".equals(sortType)
                && !"size-asc".equals(sortType)
        ) {
            sortType = "date-desc";
        }


        // ==================================================
        // 프로젝트 상태
        // ==================================================

        String projectStatus =
                attachDao.selectProjectStatus(
                        projectNo
                );


        // ==================================================
        // 검색어
        // ==================================================

        String trimmedKeyword = null;

        if (
                keyword != null
                && !keyword.trim().isEmpty()
        ) {

            trimmedKeyword =
                    keyword.trim();
        }


        // ==================================================
        // ROWNUM
        // ==================================================

        int beginRownum =
                (page - 1) * size + 1;

        int endRownum =
                page * size;


        // ==================================================
        // 파일 목록
        // ==================================================

        List<AttachDto> files;

        int totalCount;


        if (trimmedKeyword == null) {

            files =
                    attachService.list(
                            projectNo,
                            beginRownum,
                            endRownum,
                            sortType
                    );

            totalCount =
                    attachService.count(
                            projectNo
                    );

        } else {

            files =
                    attachService.list(
                            projectNo,
                            trimmedKeyword,
                            searchType,
                            beginRownum,
                            endRownum,
                            sortType
                    );

            totalCount =
                    attachService.count(
                            projectNo,
                            trimmedKeyword,
                            searchType
                    );
        }


        // ==================================================
        // 전체 페이지
        // ==================================================

        int totalPages =
                totalCount == 0
                        ? 0
                        : (int) Math.ceil(
                                (double) totalCount / size
                        );


        // ==================================================
        // 페이지 범위 보정
        // ==================================================

        if (
                totalPages > 0
                && page > totalPages
        ) {

            page = totalPages;

            beginRownum =
                    (page - 1) * size + 1;

            endRownum =
                    page * size;


            if (trimmedKeyword == null) {

                files =
                        attachService.list(
                                projectNo,
                                beginRownum,
                                endRownum,
                                sortType
                        );

            } else {

                files =
                        attachService.list(
                                projectNo,
                                trimmedKeyword,
                                searchType,
                                beginRownum,
                                endRownum,
                                sortType
                        );
            }
        }


        // ==================================================
        // 로그인 사용자 역할
        // ==================================================

        String loginRole = null;

        try {

            int empNo =
                    Integer.parseInt(
                            loginUser
                    );

            loginRole =
                    projectMemberDao.selectRole(
                            projectNo,
                            empNo
                    );

        } catch (NumberFormatException e) {

            log.warn(
                    "로그인 사용자 번호 변환 실패: {}",
                    loginUser
            );
        }


        // ==================================================
        // 로그
        // ==================================================

        log.debug(
                "프로젝트 파일 목록 조회"
        );

        log.debug(
                "projectNo = {}",
                projectNo
        );

        log.debug(
                "현재 로그인 사용자 = {}",
                loginUser
        );

        log.debug(
                "현재 사용자 역할 = {}",
                loginRole
        );

        log.debug(
                "프로젝트 상태 = {}",
                projectStatus
        );

        log.debug(
                "검색어 = {}",
                trimmedKeyword
        );

        log.debug(
                "검색 종류 = {}",
                searchType
        );

        log.debug(
                "정렬 종류 = {}",
                sortType
        );

        log.debug(
                "현재 페이지 = {}",
                page
        );

        log.debug(
                "페이지 크기 = {}",
                size
        );

        log.debug(
                "beginRownum = {}",
                beginRownum
        );

        log.debug(
                "endRownum = {}",
                endRownum
        );

        log.debug(
                "전체 파일 수 = {}",
                totalCount
        );

        log.debug(
                "전체 페이지 수 = {}",
                totalPages
        );


        // ==================================================
        // 응답
        // ==================================================

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "files",
                files
        );

        response.put(
                "totalCount",
                totalCount
        );

        response.put(
                "page",
                page
        );

        response.put(
                "size",
                size
        );

        response.put(
                "totalPages",
                totalPages
        );

        response.put(
                "loginUser",
                loginUser
        );

        response.put(
                "loginRole",
                loginRole == null
                        ? ""
                        : loginRole
        );

        response.put(
                "projectStatus",
                projectStatus == null
                        ? ""
                        : projectStatus
        );

        return ResponseEntity.ok(
                response
        );
    }

}