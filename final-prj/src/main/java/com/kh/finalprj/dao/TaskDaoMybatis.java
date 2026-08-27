package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.TaskDto;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;

@Repository
public class TaskDaoMybatis implements TaskDao {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public int sequence() {
        return sqlSession.selectOne("mapper.task.sequence");
    }

    @Override
    public int add(TaskDto taskDto) {
        return sqlSession.insert("mapper.task.add", taskDto);
    }

    @Override
    public TaskDetailResponseVO selectOne(int taskNo) {
        return sqlSession.selectOne("mapper.task.selectOne", taskNo);
    }

    @Override
    public List<TaskDto> selectByProjectNo(int projectNo) {
        return sqlSession.selectList("mapper.task.selectByProjectNo", projectNo);
    }

    @Override
    public int shiftOrders(Map<String, Object> params) {
        return sqlSession.update("mapper.task.shiftOrders", params);
    }

    @Override
    public boolean updatePosition(int taskNo, String taskStatus, int taskOrder) {
        Map<String, Object> params = new HashMap<>();
        params.put("taskNo", taskNo);
        params.put("taskStatus", taskStatus);
        params.put("taskOrder", taskOrder);
        return sqlSession.update("mapper.task.updatePosition", params) > 0;
    }

    @Override
    public boolean update(TaskDto taskDto) {
        return sqlSession.update("mapper.task.update", taskDto) > 0;
    }

    @Override
    public boolean delete(int taskNo) {
        return sqlSession.delete("mapper.task.delete", taskNo) > 0;
    }
}