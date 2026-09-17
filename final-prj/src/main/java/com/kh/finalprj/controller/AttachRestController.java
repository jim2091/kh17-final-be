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

        log.debug("projectNo = {}", projectNo);
        log.debug("uploader = {}", uploader);
        log.debug("source = {}", source);
        log.debug("sourceNo = {}", sourceNo);

        // ==================================================
        // 로그인 검사
        // ==================================================

        if (
                uploader == null
                || uploader.trim().isEmpty()
        ) {
            throw new IllegalStateException(
                    "로그인 사용자 정보가 없습니다."
            );
        }


        // ==================================================
        // 프로젝트 상태 검사
        // ==================================================

        String projectStatus =
                attachDao.selectProjectStatus(
                        projectNo
                );

        log.debug(
                "업로드 프로젝트 상태 = {}",
                projectStatus
        );

        if ("closed".equalsIgnoreCase(projectStatus)) {
            throw new IllegalStateException(
                    "종료된 프로젝트에는 파일을 업로드할 수 없습니다."
            );
        }


        // ==================================================
        // 파일 저장
        // ==================================================

        return attachService.save(
                projectNo,
                attach,
                uploader,
                source,
                sourceNo
        );
    }


    // ==================================================
    // 파일 다운로드
    // ==================================================

    @GetMapping("/{attachNo}")
    public ResponseEntity<?> download(
            @PathVariable int attachNo
    ) throws IOException {

        log.debug("파일 다운로드 요청");
        log.debug("attachNo = {}", attachNo);

        log.debug(
                "현재 profile = {}",
                environment.getActiveProfiles()
        );


        // ==================================================
        // Cloud
        // ==================================================

        if (environment.matchesProfiles("cloud")) {

            return ResponseEntity
                    .status(302)
                    .location(
                            URI.create(
                                    "./p/" + attachNo
                            )
                    )
                    .build();
        }


        // ==================================================
        // Local
        // ==================================================

        AttachInfoVO vo =
                attachService.load(attachNo);

        if (
                vo == null
                || vo.getAttachDto() == null
        ) {

            log.warn(
                    "첨부파일 정보를 찾을 수 없습니다. attachNo = {}",
                    attachNo
            );

            throw new TargetNotfoundException(
                    "첨부파일을 찾을 수 없습니다."
            );
        }

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_TYPE,
                        vo.getAttachDto().getAttachType()
                )
                .contentLength(
                        vo.getAttachDto().getAttachSize()
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition
                                .attachment()
                                .filename(
                                        vo.getAttachDto().getAttachName(),
                                        StandardCharsets.UTF_8
                                )
                                .build()
                                .toString()
                )
                .body(
                        vo.getResource()
                );
    }


    // ==================================================
    // Cloud 파일 다운로드
    // ==================================================

    @GetMapping("/p/{attachNo}")
    public ResponseEntity<?> presigned(
            @PathVariable int attachNo
    ) {

        log.debug("Cloud 파일 다운로드 요청");
        log.debug("attachNo = {}", attachNo);

        AttachDto attachDto =
                attachDao.selectOne(attachNo);

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


        String objectKey =
                storageProperties.getAwsRoot()
                        + "/"
                        + attachNo;


        GetObjectRequest request =
                GetObjectRequest
                        .builder()
                        .bucket(
                                storageProperties
                                        .getAwsBucket()
                        )
                        .key(objectKey)
                        .responseContentDisposition(
                                ContentDisposition
                                        .attachment()
                                        .filename(
                                                attachDto.getAttachName(),
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
                        .getObjectRequest(request)
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
    //
    // owner   → 모든 파일 삭제 가능
    // manager → 모든 파일 삭제 가능
    // member  → 본인이 올린 파일만 삭제 가능
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
    // 프로젝트 파일 목록 조회
    // + 검색
    // + 페이징
    // + 정렬
    //
    // searchType
    //
    // name     = 파일명
    // source   = 출처
    // uploader = 업로더
    // type     = 파일 형태
    //
    // sortType
    //
    // date-desc = 최신순
    // date-asc  = 오래된순
    //
    // page
    // 1부터 시작
    //
    // size
    // 한 페이지에 보여줄 파일 개수
    // 기본값 12
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
        // page / size 안전성 검사
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
        // 검색 타입 검증
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
        // 정렬 타입 검증
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
        // 프로젝트 상태 조회
        // ==================================================

        String projectStatus =
                attachDao.selectProjectStatus(
                        projectNo
                );


        // ==================================================
        // 검색어 정리
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
        // ROWNUM 계산
        //
        // page 1 / size 12
        // begin = 1
        // end   = 12
        //
        // page 2
        // begin = 13
        // end   = 24
        //
        // page 3
        // begin = 25
        // end   = 36
        // ==================================================

        int beginRownum =
                (page - 1) * size + 1;

        int endRownum =
                page * size;


        // ==================================================
        // 파일 목록 + 전체 개수
        //
        // ★ 정렬값을 Service까지 전달
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

        }
        else {

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
        // 전체 페이지 수
        // ==================================================

        int totalPages =
                totalCount == 0
                        ? 0
                        : (int) Math.ceil(
                                (double) totalCount / size
                        );


        // ==================================================
        // 현재 페이지가 전체 페이지보다 큰 경우
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

            }
            else {

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

        }
        catch (NumberFormatException e) {

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

        return ResponseEntity.ok(response);
    }

}