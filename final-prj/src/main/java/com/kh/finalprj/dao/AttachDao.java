package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.vo.attach.AttachProfileVO;

public interface AttachDao {

    int sequence();

    void insert(AttachDto attachDto);

    AttachDto selectOne(int attachNo);

    AttachDto selectOne(Integer attachNo);

    boolean delete(int attachNo);

    List<AttachDto> selectList(List<Integer> attachNumbers);

    // 프로젝트별 파일 조회
    List<AttachDto> selectListByProject(int projectNo);

    // 프로젝트별 파일 검색
    List<AttachDto> selectListByProjectAndKeyword(
            int projectNo,
            String keyword
    );
    
    //회원 프로필 등록용 (민영)
    void insert(AttachProfileVO attachProfileVO);
}