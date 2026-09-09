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
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.vo.attach.AttachInfoVO;
import com.kh.finalprj.vo.attach.AttachProfileVO;

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
    private ProjectMemberDao projectMemberDao;

    @Autowired
    private S3Client s3Client;

    @Autowired
    private StorageProperties storageProperties;


    // =========================================================
    // 1. 프로젝트 파일 저장
    // =========================================================

    @Transactional
    @Override
    public int save(
            int projectNo,
            MultipartFile attach,
            String uploader,
            String source,
            Integer sourceNo
    ) throws IllegalStateException, IOException {

        if (attach == null || attach.isEmpty()) {
            return 0;
        }

        if (
                uploader == null ||
                uploader.trim().isEmpty()
        ) {
            throw new IllegalStateException(
                    "파일 업로더 정보가 없습니다."
            );
        }

        if (
                source == null ||
                source.trim().isEmpty()
        ) {
            source = "FILE";
        }

        int attachNo = attachDao.sequence();

        AttachDto dto = AttachDto.builder()
                .attachNo(attachNo)
                .attachName(
                        attach.getOriginalFilename()
                )
                .attachType(
                        attach.getContentType()
                )
                .attachSize(
                        attach.getSize()
                )
                .attachUploader(
                        uploader
                )
                .attachSource(
                        source
                )
                .attachSourceNo(
                        sourceNo
                )
                .build();

        // ATTACH 저장
        attachDao.insert(dto);

        // PROJECT_FILE 연결
        projectFileDao.insert(
                projectNo,
                attachNo
        );

        // S3 저장
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

        if (
                uploader == null ||
                uploader.trim().isEmpty()
        ) {
            throw new IllegalStateException(
                    "로그인 사용자 정보가 없습니다."
            );
        }

        AttachDto attachDto =
                attachDao.selectOne(attachNo);

        if (attachDto == null) {
            throw new TargetNotfoundException();
        }


        // =====================================================
        // 파일이 어느 프로젝트에 연결되어 있는지 확인
        // =====================================================

        Integer projectNo =
                attachDao.selectProjectNo(attachNo);


        // =====================================================
        // 삭제 권한 검사
        //
        // 1. 업로더 본인
        // 2. 프로젝트 owner
        // 3. 프로젝트 manager
        //
        // 프로필 파일처럼 project_file 연결이 없는 파일은
        // 업로더 본인만 삭제 가능
        // =====================================================

        boolean isUploader =
                uploader.equals(
                        attachDto.getAttachUploader()
                );


        boolean isOwnerOrManager = false;


        if (projectNo != null) {

            try {

                int empNo =
                        Integer.parseInt(
                                uploader
                        );

                String role =
                        projectMemberDao.selectRole(
                                projectNo,
                                empNo
                        );

                isOwnerOrManager =
                        "owner".equalsIgnoreCase(role)
                        ||
                        "manager".equalsIgnoreCase(role);

            } catch (NumberFormatException e) {

                log.warn(
                        "로그인 사용자 번호 변환 실패: {}",
                        uploader
                );

            }
        }


        // =====================================================
        // 최종 권한 검사
        // =====================================================

        if (
                !isUploader &&
                !isOwnerOrManager
        ) {

            throw new IllegalStateException(
                    "파일을 삭제할 권한이 없습니다."
            );
        }


        // =====================================================
        // DB 삭제
        // PROJECT_FILE은 FK ON DELETE CASCADE
        // =====================================================

        attachDao.delete(attachNo);


        // =====================================================
        // S3 삭제
        // =====================================================

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
                response.sdkHttpResponse()
                        .statusCode()
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


    // =========================================================
    // 6. 회원 프로필 사진 저장
    // =========================================================

    @Transactional
    @Override
    public int save(
            MultipartFile attach,
            String empName,
            String source
    ) throws IllegalStateException, IOException {

        if (attach == null || attach.isEmpty()) {
            return 0;
        }

        int attachNo =
                attachDao.sequence();

        attachDao.insert(
                AttachProfileVO.builder()
                        .attachNo(attachNo)
                        .attachName(
                                attach.getOriginalFilename()
                        )
                        .attachType(
                                attach.getContentType()
                        )
                        .attachSize(
                                attach.getSize()
                        )
                        .attachUploader(
                                empName
                        )
                        .attachSource(
                                source
                        )
                        .build()
        );

        File dir =
                storageProperties.getLocalRoot();

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File target =
                new File(
                        dir,
                        String.valueOf(
                                attachNo
                        )
                );

        attach.transferTo(target);

        return attachNo;
    }

}