package com.kh.finalprj.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.vo.attach.AttachInfoVO;

public interface AttachService {
    int save(MultipartFile attach) throws IllegalStateException, IOException;
    int save(int projectNo, MultipartFile attach, String uploader, String source) throws IllegalStateException, IOException;
    
    // 만약 uploader 검증이 필요하다면 인터페이스에 이렇게 정의되어 있어야 합니다:
    void delete(Integer attachNo, String uploader);
    
    // 혹은 단일 파라미터로만 지우고 싶다면 인터페이스에 이렇게 정의되어 있어야 합니다:
    // void delete(Integer attachNo);
    
    AttachInfoVO load(int attachNo) throws IOException;
    List<AttachDto> list(int projectNo);
    List<AttachDto> list(int projectNo, String keyword);
}