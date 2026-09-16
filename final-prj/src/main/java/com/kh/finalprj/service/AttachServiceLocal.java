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

        // =====================================================
        // 1-1. 파일 확인
        // =====================================================

        if (attach == null || attach.isEmpty()) {
            return 0;
        }


        // =====================================================
        // 1-2. 업로더 확인
        // =====================================================

        if (uploader == null || uploader.trim().isEmpty()) {
            throw new IllegalStateException(
                "파일 업로더 정보가 없습니다."
            );
        }


        // =====================================================
        // 1-3. 프로젝트 존재 여부 확인
        // =====================================================

        ProjectDto project =
            projectDao.selectProject(projectNo);

        if (project == null) {
            throw new TargetNotfoundException();
        }


        // =====================================================
        // 1-4. 프로젝트 종료 여부 확인
        // =====================================================

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


        // =====================================================
        // 2. 출처 기본값
        // =====================================================

        if (source == null || source.trim().isEmpty()) {
            source = "FILE";
        }


        // =====================================================
        // 3. ATTACH 번호 생성
        // =====================================================

        int attachNo = attachDao.sequence();

        AttachDto dto =
            AttachDto.builder()
                .attachNo(attachNo)
                .attachName(attach.getOriginalFilename())
                .attachType(attach.getContentType())
                .attachSize(attach.getSize())
                .attachUploader(uploader)
                .attachSource(source)
                .attachSourceNo(sourceNo)
                .build();


        // =====================================================
        // 4. ATTACH 저장
        // =====================================================

        attachDao.insert(dto);


        // =====================================================
        // 5. PROJECT_FILE 연결
        // =====================================================

        projectFileDao.insert(
            projectNo,
            attachNo
        );


        // =====================================================
        // 6. 실제 파일 저장
        // =====================================================

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
    public AttachInfoVO load(int attachNo)
        throws IOException {

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
            FileCopyUtils.copyToByteArray(target);

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
    //
    // owner / manager
    // → 다른 사람이 올린 파일도 삭제 가능
    //
    // member
    // → 본인이 올린 파일만 삭제 가능
    //
    // closed 프로젝트
    // → 모든 사용자의 파일 삭제 불가
    //
    // 기록에서 참조 중인 파일
    // → 모든 사용자의 파일 삭제 불가
    //
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


        // =====================================================
        // 로그인 사용자 확인
        // =====================================================

        if (
            uploader == null
            || uploader.trim().isEmpty()
        ) {
            throw new IllegalStateException(
                "로그인 사용자 정보가 없습니다."
            );
        }


        // =====================================================
        // 파일 존재 여부 확인
        // =====================================================

        AttachDto attachDto =
            attachDao.selectOne(attachNo);

        if (attachDto == null) {
            throw new TargetNotfoundException();
        }


        // =====================================================
        // 프로젝트 번호 조회
        // =====================================================

        Integer projectNo =
            attachDao.selectProjectNo(attachNo);


        // =====================================================
        // 프로젝트 종료 여부 확인
        // =====================================================

        if (projectNo != null) {

            String projectStatus =
                attachDao.selectProjectStatus(projectNo);

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


        // =====================================================
        // ★ 기록에서 파일을 참조하고 있는지 확인
        // =====================================================
        //
        // project_record_attach에
        // 해당 attach_no가 하나라도 존재하면
        // 파일을 삭제할 수 없다.
        //
        // 반드시 실제 DELETE보다 먼저 검사한다.
        // =====================================================

        if (
            attachDao.existsProjectRecordAttach(
                attachNo
            )
        ) {
            throw new IllegalStateException(
                "기록에서 참조 중인 파일은 삭제할 수 없습니다."
            );
        }


        // =====================================================
        // 업로더 본인 여부
        // =====================================================

        boolean isUploader =
            uploader.equals(
                attachDto.getAttachUploader()
            );


        // =====================================================
        // OWNER / MANAGER 여부
        // =====================================================

        boolean isOwnerOrManager = false;

        if (projectNo != null) {

            try {

                int empNo =
                    Integer.parseInt(uploader);

                String role =
                    projectMemberDao.selectRole(
                        projectNo,
                        empNo
                    );

                isOwnerOrManager =
                    "owner".equalsIgnoreCase(role)
                    || "manager".equalsIgnoreCase(role);

            } catch (NumberFormatException e) {

                // 사번이 숫자가 아니면
                // owner / manager 권한을 인정하지 않음

            }
        }


        // =====================================================
        // 최종 권한 검사
        // =====================================================

        if (
            !isUploader
            && !isOwnerOrManager
        ) {
            throw new IllegalStateException(
                "파일을 삭제할 권한이 없습니다."
            );
        }


        // =====================================================
        // PROJECT_FILE 연결 삭제
        // =====================================================

        attachDao.deleteProjectFile(
            attachNo
        );


        // =====================================================
        // ATTACH 삭제
        // =====================================================

        attachDao.delete(
            attachNo
        );


        // =====================================================
        // 실제 파일 삭제
        // =====================================================

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
    // 4. 프로젝트 파일 목록
    // =========================================================

    @Override
    public List<AttachDto> list(int projectNo) {

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