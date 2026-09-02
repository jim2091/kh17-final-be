package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.TaskCollaboDto;

@Repository
public class TaskCollaboDaoMybatis implements TaskCollaboDao {

    @Autowired
    private SqlSession sqlSession;

    // 1. 단건 협업자 등록
    @Override
    public void add(int taskNo, int projectMemberNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("taskNo", taskNo);
        params.put("projectMemberNo", projectMemberNo);
        sqlSession.insert("mapper.taskcollabo.add", params);
    }

    // 2. 특정 업무의 협업자 목록 조회
    @Override
    public List<TaskCollaboDto> selectByTaskNo(int taskNo) {
        return sqlSession.selectList("mapper.taskcollabo.selectByTaskNo", taskNo);
    }

    // 3. 지정 가능한 협업자 후보 목록 조회
    @Override
    public List<TaskCollaboDto> selectAvailableCollaborators(int projectNo, int taskNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("projectNo", projectNo);
        params.put("taskNo", taskNo);
        return sqlSession.selectList("mapper.taskcollabo.selectAvailableCollaborators", params);
    }

    // 4. 특정 업무의 협업자 전체 삭제
    @Override
    public boolean deleteByTaskNo(int taskNo) {
        return sqlSession.delete("mapper.taskcollabo.deleteByTaskNo", taskNo) > 0;
    }

    // 5. 특정 협업자 1명만 삭제
    @Override
    public boolean deleteOne(int taskNo, int projectMemberNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("taskNo", taskNo);
        params.put("projectMemberNo", projectMemberNo);
        return sqlSession.delete("mapper.taskcollabo.deleteOne", params) > 0;
    }
}