package com.kh.finalprj.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.vo.attach.AttachProfileVO;

@Repository
public class AttachDaoMybatis implements AttachDao {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public int sequence() {
        return sqlSession.selectOne("mapper.attach.sequence");
    }

    // =========================================================
    // 프로젝트 파일 등록
    // =========================================================

    @Override
    public void insert(AttachDto attachDto) {
        sqlSession.insert(
                "mapper.attach.addProjectFile",
                attachDto
        );
    }

    // =========================================================
    // 파일 단건 조회
    // =========================================================

    @Override
    public AttachDto selectOne(int attachNo) {
        return sqlSession.selectOne(
                "mapper.attach.find",
                attachNo
        );
    }

    @Override
    public AttachDto selectOne(Integer attachNo) {

        if (attachNo == null) {
            return null;
        }

        return sqlSession.selectOne(
                "mapper.attach.find",
                attachNo
        );
    }

    // =========================================================
    // 파일 삭제
    // =========================================================

    @Override
    public boolean delete(int attachNo) {

        return sqlSession.delete(
                "mapper.attach.delete",
                attachNo
        ) > 0;
    }

    // =========================================================
    // 파일 목록
    // =========================================================

    @Override
    public List<AttachDto> selectList(
            List<Integer> attachNumbers
    ) {

        if (
                attachNumbers == null ||
                attachNumbers.isEmpty()
        ) {
            return List.of();
        }

        return sqlSession.selectList(
                "mapper.attach.findList",
                attachNumbers
        );
    }

    // =========================================================
    // 프로젝트별 파일 조회
    // =========================================================

    @Override
    public List<AttachDto> selectListByProject(
            int projectNo
    ) {

        return sqlSession.selectList(
                "mapper.attach.selectListByProject",
                projectNo
        );
    }

    // =========================================================
    // 프로젝트별 파일 검색
    // =========================================================

    @Override
    public List<AttachDto> selectListByProjectAndKeyword(
            int projectNo,
            String keyword
    ) {

        return sqlSession.selectList(
                "mapper.attach.selectListByProjectAndKeyword",
                new ProjectFileSearch(
                        projectNo,
                        keyword
                )
        );
    }

    // =========================================================
    // 첨부파일이 연결된 프로젝트 번호 조회
    // =========================================================

    @Override
    public Integer selectProjectNo(int attachNo) {

        return sqlSession.selectOne(
                "mapper.attach.selectProjectNo",
                attachNo
        );
    }

    // =========================================================
    // 프로젝트 파일 검색용 객체
    // =========================================================

    private static class ProjectFileSearch {

        private final int projectNo;
        private final String keyword;

        public ProjectFileSearch(
                int projectNo,
                String keyword
        ) {
            this.projectNo = projectNo;
            this.keyword = keyword;
        }

        public int getProjectNo() {
            return projectNo;
        }

        public String getKeyword() {
            return keyword;
        }
    }

    // =========================================================
    // 회원 프로필 사진
    //
    // 기존 코드 그대로 유지
    // =========================================================

    @Override
    public void insert(
            AttachProfileVO attachProfileVO
    ) {

        sqlSession.insert(
                "mapper.attach.add",
                attachProfileVO
        );
    }

}		