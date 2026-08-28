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
import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.vo.attach.AttachInfoVO;

@Service
@Profile("local")
public class AttachServiceLocal implements AttachService {
    @Autowired
    private AttachDao attachDao;
    
    @Autowired
    private StorageProperties storageProperties;
    
    // [추가] 인터페이스 규격을 맞추기 위한 일반 save 메서드
    @Transactional
    @Override
    public int save(MultipartFile attach) throws IllegalStateException, IOException {
        return save(0, attach, null, null);
    }
    
    @Transactional
    @Override
    public int save(int projectNo, MultipartFile attach, String uploader, String source) throws IllegalStateException, IOException {
        if(attach == null || attach.isEmpty()) return 0;
        
        int attachNo = attachDao.sequence();
        
        attachDao.insert(AttachDto.builder()
                    .attachNo(attachNo)
                    .projectNo(projectNo)
                    .attachName(attach.getOriginalFilename())
                    .attachType(attach.getContentType())
                    .attachSize(attach.getSize())
                    .attachUploader(uploader)
                    .attachSource(source)
                .build());
        
        File dir = storageProperties.getLocalRoot();
        dir.mkdirs(); // mkdir()보다 mkdirs()가 하위 폴더 생성에 안전합니다.
        
        File target = new File(dir, String.valueOf(attachNo));
        attach.transferTo(target);
        
        return attachNo;
    }
    
    @Override
    public AttachInfoVO load(int attachNo) throws IOException {
        AttachDto attachDto = attachDao.selectOne(attachNo);
        if(attachDto == null) throw new TargetNotfoundException();
        
        File dir = storageProperties.getLocalRoot();
        if(dir.exists() == false) throw new TargetNotfoundException();
        
        File target = new File(dir, String.valueOf(attachDto.getAttachNo()));
        if(target.exists() == false) throw new TargetNotfoundException();
        
        byte[] data = FileCopyUtils.copyToByteArray(target);
        Resource resource = new ByteArrayResource(data);
        
        return AttachInfoVO.builder()
                    .attachDto(attachDto)
                    .resource(resource)
                .build();
    }
    
    @Transactional
    @Override
    public void delete(Integer attachNo, String uploader) {

        if (attachNo == null) {
            return;
        }

        // 삭제할 파일 조회
        AttachDto attachDto = attachDao.selectOne(attachNo);

        if (attachDto == null) {
            throw new TargetNotfoundException();
        }

        // 로그인 사용자 확인
        if (uploader == null || uploader.trim().isEmpty()) {
            throw new IllegalStateException(
                "로그인 사용자 정보가 없습니다."
            );
        }

        // 본인이 올린 파일인지 확인
        if (!uploader.equals(attachDto.getAttachUploader())) {
            throw new IllegalStateException(
                "본인이 업로드한 파일만 삭제할 수 있습니다."
            );
        }

        // DB 삭제
        attachDao.delete(attachNo);

        // 실제 파일 삭제
        File dir = storageProperties.getLocalRoot();

        if (dir.exists()) {
            File target = new File(
                dir,
                String.valueOf(attachNo)
            );

            if (target.exists()) {
                target.delete();
            }
        }
    }

    
    @Override
    public List<AttachDto> list(int projectNo){
        return attachDao.selectListByProject(projectNo);
    }
    
    @Override
    public List<AttachDto> list(int projectNo, String keyword){
        return attachDao.selectListByProjectAndKeyword(projectNo, keyword);
    }
}