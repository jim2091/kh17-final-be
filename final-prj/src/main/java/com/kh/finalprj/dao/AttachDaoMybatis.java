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


    // ==================================================
    // 첨부파일 번호 생성
    // ==================================================

    @Override
    public int sequence() {

        return sqlSession.selectOne(
                "mapper.attach.sequence"
        );

    }


    // ==================================================
    // 프로젝트 파일 등록
    // ==================================================

    @Override
    public void insert(
            AttachDto attachDto
    ) {

        sqlSession.insert(
                "mapper.attach.addProjectFile",
                attachDto
        );

    }


    // ==================================================
    // 파일 하나 조회
    // ==================================================

    @Override
    public AttachDto selectOne(
            int attachNo
    ) {

        return sqlSession.selectOne(
                "mapper.attach.find",
                attachNo
        );

    }


    @Override
    public AttachDto selectOne(
            Integer attachNo
    ) {

        if (attachNo == null) {
            return null;
        }

        return sqlSession.selectOne(
                "mapper.attach.find",
                attachNo
        );

    }


    // ==================================================
    // 파일 삭제
    // ==================================================

    @Override
    public boolean delete(
            int attachNo
    ) {

        return sqlSession.delete(
                "mapper.attach.delete",
                attachNo
        ) > 0;

    }


    // ==================================================
    // 파일함 연결 삭제
    // ==================================================

    @Override
    public boolean deleteProjectFile(
            int attachNo
    ) {

        return sqlSession.delete(
                "mapper.attach.deleteProjectFile",
                attachNo
        ) > 0;

    }


    // ==================================================
    // 파일 번호 여러 개 조회
    // ==================================================

    @Override
    public List<AttachDto> selectList(
            List<Integer> attachNumbers
    ) {

        if (
                attachNumbers == null
                || attachNumbers.isEmpty()
        ) {

            return List.of();

        }

        return sqlSession.selectList(
                "mapper.attach.findList",
                attachNumbers
        );

    }


    // ==================================================
    // 프로젝트 전체 파일
    // 기존 메서드
    // ==================================================

    @Override
    public List<AttachDto> selectListByProject(
            int projectNo
    ) {

        return sqlSession.selectList(
                "mapper.attach.selectListByProject",
                projectNo
        );

    }


    // ==================================================
    // 프로젝트 파일 검색
    // 기존 메서드
    // ==================================================

    @Override
    public List<AttachDto> selectListByProjectAndKeyword(
            int projectNo,
            String keyword,
            String searchType
    ) {

        ProjectFileSearch search =
                new ProjectFileSearch(
                        projectNo,
                        keyword,
                        searchType
                );

        return sqlSession.selectList(
                "mapper.attach.selectListByProjectAndKeyword",
                search
        );

    }


    // ==================================================
    // 프로젝트 파일 페이징
    // 기존 메서드
    //
    // 기존 Local 서비스가 호출하므로 반드시 유지
    // ==================================================

    @Override
    public List<AttachDto> selectListByProject(
            int projectNo,
            int beginRownum,
            int endRownum
    ) {

        ProjectFilePage search =
                new ProjectFilePage(
                        projectNo,
                        beginRownum,
                        endRownum,
                        "date-desc"
                );

        return sqlSession.selectList(
                "mapper.attach.selectListByProjectPage",
                search
        );

    }


    // ==================================================
    // 프로젝트 파일 페이징 + 정렬
    // ==================================================

    @Override
    public List<AttachDto> selectListByProject(
            int projectNo,
            int beginRownum,
            int endRownum,
            String sortType
    ) {

        ProjectFilePage search =
                new ProjectFilePage(
                        projectNo,
                        beginRownum,
                        endRownum,
                        sortType
                );

        return sqlSession.selectList(
                "mapper.attach.selectListByProjectPage",
                search
        );

    }


    // ==================================================
    // 프로젝트 검색 + 페이징
    // 기존 메서드
    // ==================================================

    @Override
    public List<AttachDto> selectListByProjectAndKeyword(
            int projectNo,
            String keyword,
            String searchType,
            int beginRownum,
            int endRownum
    ) {

        ProjectFilePage search =
                new ProjectFilePage(
                        projectNo,
                        keyword,
                        searchType,
                        beginRownum,
                        endRownum,
                        "date-desc"
                );

        return sqlSession.selectList(
                "mapper.attach.selectListByProjectAndKeywordPage",
                search
        );

    }


    // ==================================================
    // 프로젝트 검색 + 페이징 + 정렬
    // ==================================================

    @Override
    public List<AttachDto> selectListByProjectAndKeyword(
            int projectNo,
            String keyword,
            String searchType,
            int beginRownum,
            int endRownum,
            String sortType
    ) {

        ProjectFilePage search =
                new ProjectFilePage(
                        projectNo,
                        keyword,
                        searchType,
                        beginRownum,
                        endRownum,
                        sortType
                );

        return sqlSession.selectList(
                "mapper.attach.selectListByProjectAndKeywordPage",
                search
        );

    }


    // ==================================================
    // 프로젝트 전체 파일 개수
    // ==================================================

    @Override
    public int countByProject(
            int projectNo
    ) {

        Integer count =
                sqlSession.selectOne(
                        "mapper.attach.countByProject",
                        projectNo
                );

        return count == null
                ? 0
                : count;

    }


    // ==================================================
    // 프로젝트 검색 결과 개수
    // ==================================================

    @Override
    public int countByProjectAndKeyword(
            int projectNo,
            String keyword,
            String searchType
    ) {

        ProjectFileSearch search =
                new ProjectFileSearch(
                        projectNo,
                        keyword,
                        searchType
                );

        Integer count =
                sqlSession.selectOne(
                        "mapper.attach.countByProjectAndKeyword",
                        search
                );

        return count == null
                ? 0
                : count;

    }


    // ==================================================
    // 파일이 속한 프로젝트 번호
    // ==================================================

    @Override
    public Integer selectProjectNo(
            int attachNo
    ) {

        return sqlSession.selectOne(
                "mapper.attach.selectProjectNo",
                attachNo
        );

    }


    // ==================================================
    // 프로젝트 상태 조회
    // ==================================================

    @Override
    public String selectProjectStatus(
            int projectNo
    ) {

        return sqlSession.selectOne(
                "mapper.attach.selectProjectStatus",
                projectNo
        );

    }


    // ==================================================
    // 기록에서 파일을 참조하고 있는지 확인
    // ==================================================

    @Override
    public boolean existsProjectRecordAttach(
            int attachNo
    ) {

        Integer count =
                sqlSession.selectOne(
                        "mapper.attach.existsProjectRecordAttach",
                        attachNo
                );

        return count != null
                && count > 0;

    }


    // ==================================================
    // 검색 전달 객체
    // ==================================================

    private static class ProjectFileSearch {

        private final int projectNo;
        private final String keyword;
        private final String searchType;

        public ProjectFileSearch(
                int projectNo,
                String keyword,
                String searchType
        ) {

            this.projectNo = projectNo;
            this.keyword = keyword;
            this.searchType = searchType;

        }

        public int getProjectNo() {
            return projectNo;
        }

        public String getKeyword() {
            return keyword;
        }

        public String getSearchType() {
            return searchType;
        }

    }


    // ==================================================
    // 페이징 전달 객체
    // ==================================================

    private static class ProjectFilePage {

        private final int projectNo;
        private final String keyword;
        private final String searchType;
        private final int beginRownum;
        private final int endRownum;
        private final String sortType;


        // --------------------------------------------------
        // 전체 목록
        // --------------------------------------------------

        public ProjectFilePage(
                int projectNo,
                int beginRownum,
                int endRownum,
                String sortType
        ) {

            this(
                    projectNo,
                    null,
                    null,
                    beginRownum,
                    endRownum,
                    sortType
            );

        }


        // --------------------------------------------------
        // 검색 목록
        // --------------------------------------------------

        public ProjectFilePage(
                int projectNo,
                String keyword,
                String searchType,
                int beginRownum,
                int endRownum,
                String sortType
        ) {

            this.projectNo = projectNo;
            this.keyword = keyword;
            this.searchType = searchType;
            this.beginRownum = beginRownum;
            this.endRownum = endRownum;
            this.sortType = sortType;

        }


        public int getProjectNo() {
            return projectNo;
        }


        public String getKeyword() {
            return keyword;
        }


        public String getSearchType() {
            return searchType;
        }


        public int getBeginRownum() {
            return beginRownum;
        }


        public int getEndRownum() {
            return endRownum;
        }


        public String getSortType() {
            return sortType;
        }

    }


    // ==================================================
    // 프로필 사진 등록
    // ==================================================

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