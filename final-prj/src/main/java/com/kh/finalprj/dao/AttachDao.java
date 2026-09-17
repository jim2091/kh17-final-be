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

    void insert(
            AttachDto attachDto
    );


    // ==================================================
    // 파일 하나 조회
    // ==================================================

    AttachDto selectOne(
            int attachNo
    );

    AttachDto selectOne(
            Integer attachNo
    );


    // ==================================================
    // 파일 삭제
    // ==================================================

    boolean delete(
            int attachNo
    );


    // ==================================================
    // 파일함 연결 삭제
    // ==================================================

    boolean deleteProjectFile(
            int attachNo
    );


    // ==================================================
    // 파일 번호 여러 개 조회
    // ==================================================

    List<AttachDto> selectList(
            List<Integer> attachNumbers
    );


    // ==================================================
    // 프로젝트별 전체 파일
    // 기존 메서드
    // ==================================================

    List<AttachDto> selectListByProject(
            int projectNo
    );


    // ==================================================
    // 프로젝트별 검색
    // 기존 메서드
    // ==================================================

    List<AttachDto> selectListByProjectAndKeyword(
            int projectNo,
            String keyword,
            String searchType
    );


    // ==================================================
    // 프로젝트별 파일 페이징
    // 기존 메서드
    //
    // 기존 ServiceLocal에서 사용 중이므로 유지
    // ==================================================

    List<AttachDto> selectListByProject(
            int projectNo,
            int beginRownum,
            int endRownum
    );


    // ==================================================
    // 프로젝트별 파일 페이징 + 정렬
    // ==================================================

    List<AttachDto> selectListByProject(
            int projectNo,
            int beginRownum,
            int endRownum,
            String sortType
    );


    // ==================================================
    // 프로젝트별 검색 + 페이징
    // 기존 메서드
    //
    // 기존 ServiceLocal에서 사용 중이므로 유지
    // ==================================================

    List<AttachDto> selectListByProjectAndKeyword(
            int projectNo,
            String keyword,
            String searchType,
            int beginRownum,
            int endRownum
    );


    // ==================================================
    // 프로젝트별 검색 + 페이징 + 정렬
    // ==================================================

    List<AttachDto> selectListByProjectAndKeyword(
            int projectNo,
            String keyword,
            String searchType,
            int beginRownum,
            int endRownum,
            String sortType
    );


    // ==================================================
    // 프로젝트별 전체 파일 개수
    // ==================================================

    int countByProject(
            int projectNo
    );


    // ==================================================
    // 프로젝트별 검색 결과 개수
    // ==================================================

    int countByProjectAndKeyword(
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
    // 기록에서 파일을 참조하고 있는지 확인
    // ==================================================

    boolean existsProjectRecordAttach(
            int attachNo
    );


    // ==================================================
    // 프로필 사진 등록
    // ==================================================

    void insert(
            AttachProfileVO attachProfileVO
    );

}