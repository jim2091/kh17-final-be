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
import com.kh.finalprj.dao.ProjectFileDao;
import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.vo.attach.AttachInfoVO;
import com.kh.finalprj.vo.attach.AttachProfileVO;

@Service
@Profile("local")
public class AttachServiceLocal
        implements AttachService {

    @Autowired
    private AttachDao attachDao;

    @Autowired
    private ProjectFileDao projectFileDao;

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
                        .attachUploader(
                                uploader
                        )
                        .attachSource(
                                source
                        )
                        .build();


        // attach 저장
        attachDao.insert(dto);


        // 프로젝트와 파일 연결
        projectFileDao.insert(
                projectNo,
                attachNo
        );


        // 실제 파일 저장
        File dir =
                storageProperties.getLocalRoot();

        dir.mkdirs();


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


        // project_file은 ON DELETE CASCADE
        // attach 삭제 시 자동 삭제
        attachDao.delete(attachNo);


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
	public int save(MultipartFile attach, String empName, String source) throws IllegalStateException, IOException{
			int attachNo = attachDao.sequence();
			attachDao.insert(
						AttachProfileVO.builder()
							.attachNo(attachNo)
							.attachName(attach.getOriginalFilename())
							.attachType(attach.getContentType())
							.attachSize(attach.getSize())
							.attachUploader(empName)
							.attachSource(source)
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