package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.dto.ProjectDto;
import com.kh.finalprj.dto.TaskDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SearchDaoMybatis implements SearchDao {

    private final SqlSession sqlSession;

    private static final String NAMESPACE =
            "com.kh.finalprj.dao.SearchDao";

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
    public List<TaskDto> searchTasks(String keyword) {

        return sqlSession.selectList(
                NAMESPACE + ".searchTasks",
                keyword
        );
    }


    // ========================================
    // 파일 검색
    // ========================================

    @Override
    public List<AttachDto> searchFiles(String keyword) {

        return sqlSession.selectList(
                NAMESPACE + ".searchFiles",
                keyword
        );
    }

}