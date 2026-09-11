package com.kh.finalprj.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.vo.attach.AttachInfoVO;

public interface AttachService {

    // ==================================================
    // 프로젝트 파일 업로드
    // ==================================================

    int save(
            int projectNo,
            MultipartFile attach,
            String uploader,
            String source,
            Integer sourceNo
    ) throws IllegalStateException, IOException;

    // ==================================================
    // 파일 삭제
    // ==================================================

    void delete(
            Integer attachNo,
            String uploader
    );

    // ==================================================
    // 파일 하나 조회
    // ==================================================

    AttachInfoVO load(
            int attachNo
    ) throws IOException;

    // ==================================================
    // 프로젝트 파일 전체 조회
    // ==================================================

    List<AttachDto> list(
            int projectNo
    );

    // ==================================================
    // 프로젝트 파일 검색
    //
    // name     = 파일명
    // source   = 출처
    // uploader = 업로더
    // type     = 파일 형태
    // ==================================================

    List<AttachDto> list(
            int projectNo,
            String keyword,
            String searchType
    );

    // ==================================================
    // 프로필 사진 저장
    // ==================================================

    int save(
            MultipartFile attach,
            String empName,
            String source
    ) throws IllegalStateException, IOException;

}