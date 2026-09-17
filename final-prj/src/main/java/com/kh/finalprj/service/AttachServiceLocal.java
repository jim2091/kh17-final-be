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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kh.finalprj.configuration.StorageProperties;
import com.kh.finalprj.dao.AttachDao;
import com.kh.finalprj.dao.ProjectDao;
import com.kh.finalprj.dao.ProjectFileDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.dto.ProjectDto;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.vo.attach.AttachInfoVO;
import com.kh.finalprj.vo.attach.AttachProfileVO;

@Service
@Profile("local")
public class AttachServiceLocal implements AttachService {

    @Autowired
    private AttachDao attachDao;

    @Autowired
    private ProjectFileDao projectFileDao;

    @Autowired
    private ProjectMemberDao projectMemberDao;

    @Autowired
    private ProjectDao projectDao;

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
                uploader == null
                || uploader.trim().isEmpty()
        ) {
            throw new IllegalStateException(
                    "파일 업로더 정보가 없습니다."
            );
        }

        ProjectDto project =
                projectDao.selectProject(projectNo);

        if (project == null) {
            throw new TargetNotfoundException();
        }

        if (
                project.getProjectStatus() != null
                && "closed".equalsIgnoreCase(
                        project.getProjectStatus()
                )
        ) {
            throw new IllegalStateException(
                    "종료된 프로젝트에는 파일을 업로드할 수 없습니다."
            );
        }

        if (
                source == null
                || source.trim().isEmpty()
        ) {
            source = "FILE";
        }

        int attachNo =
                attachDao.sequence();

        AttachDto dto =
                AttachDto.builder()
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
                        .attachUploader(uploader)
                        .attachSource(source)
                        .attachSourceNo(sourceNo)
                        .build();

        attachDao.insert(dto);

        projectFileDao.insert(
                projectNo,
                attachNo
        );


        File dir =
                storageProperties.getLocalRoot();

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File target =
                new File(
                        dir,
                        String.valueOf(attachNo)
                );

        attach.transferTo(target);

        return attachNo;
    }


    // =========================================================
    // 2. 파일 로드
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

        File dir =
                storageProperties.getLocalRoot();

        if (!dir.exists()) {
            throw new TargetNotfoundException();
        }

        File target =
                new File(
                        dir,
                        String.valueOf(
                                attachDto.getAttachNo()
                        )
                );

        if (!target.exists()) {
            throw new TargetNotfoundException();
        }

        byte[] data =
                FileCopyUtils.copyToByteArray(
                        target
                );

        Resource resource =
                new ByteArrayResource(data);

        return AttachInfoVO.builder()
                .attachDto(attachDto)
                .resource(resource)
                .build();
    }


    // =========================================================
    // 3. 파일 삭제
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
                uploader == null
                || uploader.trim().isEmpty()
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

        Integer projectNo =
                attachDao.selectProjectNo(attachNo);

        if (projectNo != null) {

            String projectStatus =
                    attachDao.selectProjectStatus(
                            projectNo
                    );

            if (
                    "closed".equalsIgnoreCase(
                            projectStatus
                    )
            ) {
                throw new IllegalStateException(
                        "종료된 프로젝트에서는 파일을 삭제할 수 없습니다."
                );
            }
        }


        // ==================================================
        // 기록에서 사용 중인지 확인
        // ==================================================

        if (
                attachDao.existsProjectRecordAttach(
                        attachNo
                )
        ) {
            throw new IllegalStateException(
                    "기록에서 참조 중인 파일은 삭제할 수 없습니다."
            );
        }


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
                        || "manager".equalsIgnoreCase(role);

            }
            catch (NumberFormatException e) {
                // 사번이 숫자가 아니면
                // owner / manager 권한을 인정하지 않음
            }
        }


        if (
                !isUploader
                && !isOwnerOrManager
        ) {
            throw new IllegalStateException(
                    "파일을 삭제할 권한이 없습니다."
            );
        }


        attachDao.deleteProjectFile(
                attachNo
        );

        attachDao.delete(
                attachNo
        );


        File dir =
                storageProperties.getLocalRoot();

        if (dir.exists()) {

            File target =
                    new File(
                            dir,
                            String.valueOf(attachNo)
                    );

            if (target.exists()) {
                target.delete();
            }
        }
    }


    // =========================================================
    // 4. 기존 프로젝트 파일 목록
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
    // 5. 기존 프로젝트 파일 검색
    // =========================================================

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


    // =========================================================
    // 6. ROWNUM 페이징 목록 + 정렬
    // =========================================================

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


    // =========================================================
    // 7. ROWNUM 페이징 + 검색 + 정렬
    // =========================================================

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


    // =========================================================
    // 8. 전체 파일 개수
    // =========================================================

    @Override
    public int count(
            int projectNo
    ) {

        return attachDao.countByProject(
                projectNo
        );
    }


    // =========================================================
    // 9. 검색 결과 개수
    // =========================================================

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


    // =========================================================
    // 10. 회원 프로필 사진 저장
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

        if (source == null || source.trim().isEmpty()) {
            source = "PROFILE";
        }

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
                        .attachUploader(empName)
                        .attachSource(source)
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
                        String.valueOf(attachNo)
                );

        attach.transferTo(target);

        return attachNo;
    }

}