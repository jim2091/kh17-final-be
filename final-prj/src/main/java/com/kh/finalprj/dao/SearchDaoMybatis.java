package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.dto.NoteSearchDto;
import com.kh.finalprj.dto.ProjectDto;
import com.kh.finalprj.dto.ProjectHistoryDto;
import com.kh.finalprj.dto.ProjectHistoryResponseDto;
import com.kh.finalprj.dto.ProjectRecordSearchDto;
import com.kh.finalprj.dto.TaskDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SearchDaoMybatis implements SearchDao {

    private final SqlSession sqlSession;

    private static final String NAMESPACE = "mapper.search";


    // ========================================
    // 사용자 검색
    // ========================================

    @Override
    public List<EmpDto> searchMembers(String keyword) {

        return sqlSession.selectList(
                NAMESPACE + ".searchMembers",
                keyword
        );
    }


    // ========================================
    // 프로젝트 검색
    // ========================================

    @Override
    public List<ProjectDto> searchProjects(
            String keyword,
            int empNo
    ) {

        Map<String, Object> params = new HashMap<>();

        params.put("keyword", keyword);
        params.put("empNo", empNo);

        return sqlSession.selectList(
                NAMESPACE + ".searchProjects",
                params
        );
    }


    // ========================================
    // 업무 검색
    // ========================================

    @Override
    public List<TaskDto> searchTasks(
            String keyword,
            int empNo
    ) {

        Map<String, Object> params = new HashMap<>();

        params.put("keyword", keyword);
        params.put("empNo", empNo);

        return sqlSession.selectList(
                NAMESPACE + ".searchTasks",
                params
        );
    }


    // ========================================
    // 기록 검색
    // ========================================

    @Override
    public List<ProjectRecordSearchDto> searchRecords(
            String keyword,
            int empNo
    ) {

        Map<String, Object> params = new HashMap<>();

        params.put("keyword", keyword);
        params.put("empNo", empNo);

        return sqlSession.selectList(
                NAMESPACE + ".searchRecords",
                params
        );
    }


    // ========================================
    // 노트 검색
    // ========================================

    @Override
    public List<NoteSearchDto> searchNotes(
            String keyword,
            int empNo
    ) {

        Map<String, Object> params = new HashMap<>();

        params.put("keyword", keyword);
        params.put("empNo", empNo);

        return sqlSession.selectList(
                NAMESPACE + ".searchNotes",
                params
        );
    }


    // ========================================
    // 파일 검색
    // ========================================

    @Override
    public List<AttachDto> searchFiles(
            String keyword,
            int empNo
    ) {

        Map<String, Object> params = new HashMap<>();

        params.put("keyword", keyword);
        params.put("empNo", empNo);

        return sqlSession.selectList(
                NAMESPACE + ".searchFiles",
                params
        );
    }


    // ========================================
    // 사용자 프로젝트 참여 이력
    // ========================================

    @Override
    public List<ProjectHistoryDto> searchProjectHistory(
            int empNo
    ) {

        return sqlSession.selectList(
                NAMESPACE + ".searchProjectHistory",
                empNo
        );
    }


    // ========================================
    // 사용자 정보 조회
    // ========================================

    @Override
    public ProjectHistoryResponseDto searchUserInfo(
            int empNo
    ) {

        return sqlSession.selectOne(
                NAMESPACE + ".searchUserInfo",
                empNo
        );
    }

}