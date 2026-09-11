package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.vo.attach.AttachProfileVO;

public interface AttachDao {

    // ==================================================
    // 첨부파일 번호 생성
    // ==================================================

    int sequence();

    // ==================================================
    // 프로젝트 파일 등록
    // ==================================================

    void insert(AttachDto attachDto);

    // ==================================================
    // 파일 하나 조회
    // ==================================================

    AttachDto selectOne(int attachNo);

    AttachDto selectOne(Integer attachNo);

    // ==================================================
    // 파일 삭제
    // ==================================================

    boolean delete(int attachNo);

    // ==================================================
    // 파일 번호 여러 개 조회
    // ==================================================

    List<AttachDto> selectList(
            List<Integer> attachNumbers
    );

    // ==================================================
    // 프로젝트별 전체 파일
    // ==================================================

    List<AttachDto> selectListByProject(
            int projectNo
    );

    // ==================================================
    // 프로젝트별 검색
    // ==================================================

    List<AttachDto> selectListByProjectAndKeyword(
            int projectNo,
            String keyword,
            String searchType
    );

    // ==================================================
    // 첨부파일이 속한 프로젝트 번호
    // ==================================================

    Integer selectProjectNo(
            int attachNo
    );

    // ==================================================
    // 프로젝트 상태 조회
    // ==================================================

    String selectProjectStatus(
            int projectNo
    );

    // ==================================================
    // 프로필 사진 등록
    // ==================================================

    void insert(
            AttachProfileVO attachProfileVO
    );

}