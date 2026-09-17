package com.kh.finalprj.service;

import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.finalprj.configuration.StorageProperties;
import com.kh.finalprj.dao.AttachDao;
import com.kh.finalprj.dao.ProjectFileDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.vo.attach.AttachInfoVO;
import com.kh.finalprj.vo.attach.AttachProfileVO;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
@Profile("cloud")
@RequiredArgsConstructor
public class AttachServiceCloud implements AttachService {

    private final AttachDao attachDao;

    private final ProjectFileDao projectFileDao;

    private final ProjectMemberDao projectMemberDao;

    private final S3Client s3Client;

    private final StorageProperties storageProperties;


    // ==================================================
    // 1. 프로젝트 파일 업로드
    // ==================================================

    @Override
    public int save(
            int projectNo,
            MultipartFile attach,
            String uploader,
            String source,
            Integer sourceNo
    ) throws IllegalStateException, IOException {

        // --------------------------------------------------
        // 파일 검사
        // --------------------------------------------------

        if (attach == null || attach.isEmpty()) {
            throw new IllegalArgumentException(
                    "업로드할 파일이 없습니다."
            );
        }

        if (uploader == null || uploader.trim().isEmpty()) {
            throw new IllegalStateException(
                    "파일 업로더 정보가 없습니다."
            );
        }


        // --------------------------------------------------
        // 프로젝트 상태 확인
        // --------------------------------------------------

        String projectStatus =
                attachDao.selectProjectStatus(projectNo);

        if (projectStatus == null) {
            throw new IllegalArgumentException(
                    "존재하지 않는 프로젝트입니다."
            );
        }

        if (!"active".equalsIgnoreCase(projectStatus)) {
            throw new IllegalStateException(
                    "진행 중인 프로젝트에서만 파일을 업로드할 수 있습니다."
            );
        }


        // --------------------------------------------------
        // source 기본값
        // --------------------------------------------------

        if (source == null || source.trim().isEmpty()) {
            source = "FILE";
        }


        // --------------------------------------------------
        // 첨부파일 번호 생성
        // --------------------------------------------------

        int attachNo =
                attachDao.sequence();


        // --------------------------------------------------
        // MIME TYPE
        // --------------------------------------------------

        String contentType =
                attach.getContentType();

        if (
                contentType == null
                || contentType.trim().isEmpty()
        ) {
            contentType =
                    "application/octet-stream";
        }


        // --------------------------------------------------
        // 원본 파일명
        // --------------------------------------------------

        String originalFilename =
                attach.getOriginalFilename();

        if (
                originalFilename == null
                || originalFilename.trim().isEmpty()
        ) {
            originalFilename = "unknown";
        }


        // ==================================================
        // ATTACH DB 저장
        // ==================================================

        AttachDto attachDto =
                AttachDto.builder()
                        .attachNo(attachNo)
                        .attachName(originalFilename)
                        .attachType(contentType)
                        .attachSize(attach.getSize())
                        .attachUploader(uploader)
                        .attachSource(source)
                        .attachSourceNo(sourceNo)
                        .build();

        attachDao.insert(attachDto);


        // ==================================================
        // PROJECT_FILE 연결
        // ==================================================

        projectFileDao.insert(
                projectNo,
                attachNo
        );


        // ==================================================
        // S3 객체 Key
        // ==================================================

        String objectKey =
                storageProperties.getAwsRoot()
                        + "/"
                        + attachNo;


        // ==================================================
        // S3 업로드 요청
        // ==================================================

        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucket(
                                storageProperties.getAwsBucket()
                        )
                        .key(objectKey)
                        .contentType(contentType)
                        .build();


        try {

            s3Client.putObject(
                    request,
                    RequestBody.fromBytes(
                            attach.getBytes()
                    )
            );

        }
        catch (Exception e) {

            // --------------------------------------------------
            // S3 업로드 실패
            // PROJECT_FILE 먼저 삭제
            // --------------------------------------------------

            try {
                attachDao.deleteProjectFile(
                        attachNo
                );
            }
            catch (Exception ignored) {
            }


            // --------------------------------------------------
            // ATTACH 삭제
            // --------------------------------------------------

            try {
                attachDao.delete(
                        attachNo
                );
            }
            catch (Exception ignored) {
            }


            throw new IOException(
                    "S3 파일 업로드에 실패했습니다.",
                    e
            );
        }


        return attachNo;
    }


    // ==================================================
    // 2. 파일 삭제
    // ==================================================

    @Override
    public void delete(
            Integer attachNo,
            String uploader
    ) {

        if (attachNo == null) {
            throw new IllegalArgumentException(
                    "파일 번호가 없습니다."
            );
        }

        if (
                uploader == null
                || uploader.trim().isEmpty()
        ) {
            throw new IllegalStateException(
                    "로그인 사용자 정보가 없습니다."
            );
        }


        // --------------------------------------------------
        // 파일 정보 조회
        // --------------------------------------------------

        AttachDto attach =
                attachDao.selectOne(attachNo);

        if (attach == null) {
            throw new IllegalArgumentException(
                    "존재하지 않는 파일입니다."
            );
        }


        // --------------------------------------------------
        // 프로젝트 번호 조회
        //
        // 일반 프로젝트 파일:
        // project_file에서 프로젝트 번호 조회
        //
        // 프로필 사진:
        // project_file 연결이 없으므로 null
        // --------------------------------------------------

        Integer projectNo =
                attachDao.selectProjectNo(attachNo);


        boolean allowed = false;


        // --------------------------------------------------
        // 업로더 본인
        // --------------------------------------------------

        if (
                uploader.equals(
                        attach.getAttachUploader()
                )
        ) {
            allowed = true;
        }


        // --------------------------------------------------
        // 프로젝트 owner / manager
        // --------------------------------------------------

        if (
                !allowed
                && projectNo != null
        ) {

            try {

                int empNo =
                        Integer.parseInt(uploader);

                String role =
                        projectMemberDao.selectRole(
                                projectNo,
                                empNo
                        );

                if (
                        "owner".equalsIgnoreCase(role)
                        || "manager".equalsIgnoreCase(role)
                ) {
                    allowed = true;
                }

            }
            catch (NumberFormatException ignored) {
                // 사번이 숫자가 아니면
                // owner / manager 권한 확인하지 않음
            }
        }


        // --------------------------------------------------
        // 권한 없음
        // --------------------------------------------------

        if (!allowed) {
            throw new IllegalStateException(
                    "파일을 삭제할 권한이 없습니다."
            );
        }


        // ==================================================
        // PROJECT_FILE 삭제
        //
        // 프로젝트 파일인 경우에만 삭제
        // 프로필 사진은 projectNo가 null이므로 실행하지 않음
        // ==================================================

        if (projectNo != null) {
            attachDao.deleteProjectFile(
                    attachNo
            );
        }


        // ==================================================
        // ATTACH DB 삭제
        // ==================================================

        attachDao.delete(
                attachNo
        );


        // ==================================================
        // S3 삭제
        // ==================================================

        String objectKey =
                storageProperties.getAwsRoot()
                        + "/"
                        + attachNo;

        DeleteObjectRequest deleteRequest =
                DeleteObjectRequest.builder()
                        .bucket(
                                storageProperties.getAwsBucket()
                        )
                        .key(objectKey)
                        .build();


        try {

            s3Client.deleteObject(
                    deleteRequest
            );

        }
        catch (S3Exception e) {

            System.err.println(
                    "S3 파일 삭제 실패 : "
                            + e.getMessage()
            );
        }
    }


    // ==================================================
    // 3. 파일 하나 조회
    // ==================================================

    @Override
    public AttachInfoVO load(
            int attachNo
    ) throws IOException {

        AttachDto attachDto =
                attachDao.selectOne(attachNo);

        if (attachDto == null) {
            throw new IllegalArgumentException(
                    "존재하지 않는 파일입니다."
            );
        }


        // --------------------------------------------------
        // S3 객체 Key
        // --------------------------------------------------

        String objectKey =
                storageProperties.getAwsRoot()
                        + "/"
                        + attachNo;


        // --------------------------------------------------
        // S3 조회 요청
        // --------------------------------------------------

        GetObjectRequest request =
                GetObjectRequest.builder()
                        .bucket(
                                storageProperties.getAwsBucket()
                        )
                        .key(objectKey)
                        .build();


        try {

            byte[] data =
                    s3Client
                            .getObjectAsBytes(request)
                            .asByteArray();


            ByteArrayResource resource =
                    new ByteArrayResource(data);


            return AttachInfoVO.builder()
                    .attachDto(attachDto)
                    .resource(resource)
                    .build();

        }
        catch (S3Exception e) {

            throw new IOException(
                    "S3에서 파일을 가져오지 못했습니다.",
                    e
            );
        }
    }


    // ==================================================
    // 4. 프로젝트 전체 파일
    // ==================================================

    @Override
    public List<AttachDto> list(
            int projectNo
    ) {

        return attachDao.selectListByProject(
                projectNo
        );
    }


    // ==================================================
    // 5. 프로젝트 파일 검색
    // ==================================================

    @Override
    public List<AttachDto> list(
            int projectNo,
            String keyword,
            String searchType
    ) {

        return attachDao.selectListByProjectAndKeyword(
                projectNo,
                keyword,
                searchType
        );
    }


    // ==================================================
    // 6. 프로젝트 파일 페이징 + 정렬
    // ==================================================

    @Override
    public List<AttachDto> list(
            int projectNo,
            int beginRownum,
            int endRownum,
            String sortType
    ) {

        return attachDao.selectListByProject(
                projectNo,
                beginRownum,
                endRownum,
                sortType
        );
    }


    // ==================================================
    // 7. 프로젝트 검색 + 페이징 + 정렬
    // ==================================================

    @Override
    public List<AttachDto> list(
            int projectNo,
            String keyword,
            String searchType,
            int beginRownum,
            int endRownum,
            String sortType
    ) {

        return attachDao.selectListByProjectAndKeyword(
                projectNo,
                keyword,
                searchType,
                beginRownum,
                endRownum,
                sortType
        );
    }


    // ==================================================
    // 8. 프로젝트 전체 파일 개수
    // ==================================================

    @Override
    public int count(
            int projectNo
    ) {

        return attachDao.countByProject(
                projectNo
        );
    }


    // ==================================================
    // 9. 프로젝트 검색 결과 개수
    // ==================================================

    @Override
    public int count(
            int projectNo,
            String keyword,
            String searchType
    ) {

        return attachDao.countByProjectAndKeyword(
                projectNo,
                keyword,
                searchType
        );
    }


    // ==================================================
    // 10. 프로필 사진 저장
    // ==================================================

    @Override
    public int save(
            MultipartFile attach,
            String empName,
            String source
    ) throws IllegalStateException, IOException {

        // --------------------------------------------------
        // 파일 검사
        // --------------------------------------------------

        if (attach == null || attach.isEmpty()) {
            throw new IllegalArgumentException(
                    "업로드할 프로필 이미지가 없습니다."
            );
        }

        if (
                empName == null
                || empName.trim().isEmpty()
        ) {
            throw new IllegalStateException(
                    "프로필 이미지 업로더 정보가 없습니다."
            );
        }


        // --------------------------------------------------
        // 프로필 source
        // --------------------------------------------------

        source = "PROFILE";


        // --------------------------------------------------
        // 첨부파일 번호 생성
        // --------------------------------------------------

        int attachNo =
                attachDao.sequence();


        // --------------------------------------------------
        // MIME TYPE
        // --------------------------------------------------

        String contentType =
                attach.getContentType();

        if (
                contentType == null
                || contentType.trim().isEmpty()
        ) {
            contentType =
                    "application/octet-stream";
        }


        // --------------------------------------------------
        // 원본 파일명
        // --------------------------------------------------

        String originalFilename =
                attach.getOriginalFilename();

        if (
                originalFilename == null
                || originalFilename.trim().isEmpty()
        ) {
            originalFilename = "profile";
        }


        // ==================================================
        // PROFILE DB 저장
        // ==================================================

        AttachProfileVO profile =
                AttachProfileVO.builder()
                        .attachNo(attachNo)
                        .attachName(originalFilename)
                        .attachType(contentType)
                        .attachSize(attach.getSize())
                        .attachUploader(empName)
                        .attachSource(source)
                        .build();

        attachDao.insert(profile);


        // ==================================================
        // S3 객체 Key
        // ==================================================

        String objectKey =
                storageProperties.getAwsRoot()
                        + "/"
                        + attachNo;


        // ==================================================
        // S3 업로드 요청
        // ==================================================

        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucket(
                                storageProperties.getAwsBucket()
                        )
                        .key(objectKey)
                        .contentType(contentType)
                        .build();


        try {

            s3Client.putObject(
                    request,
                    RequestBody.fromBytes(
                            attach.getBytes()
                    )
            );

        }
        catch (Exception e) {

            // --------------------------------------------------
            // S3 업로드 실패 시 DB 삭제
            // --------------------------------------------------

            try {
                attachDao.delete(
                        attachNo
                );
            }
            catch (Exception ignored) {
            }


            throw new IOException(
                    "프로필 이미지를 S3에 업로드하지 못했습니다.",
                    e
            );
        }


        return attachNo;
    }

}
