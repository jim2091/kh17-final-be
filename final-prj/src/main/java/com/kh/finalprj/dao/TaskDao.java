package com.kh.finalprj.dao;

import java.util.List;
import java.util.Map;
import com.kh.finalprj.dto.TaskDto;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;

public interface TaskDao {
    int sequence();
    int add(TaskDto taskDto);
    TaskDetailResponseVO selectOne(int taskNo);
    List<TaskDto> selectByProjectNo(int projectNo);
    int shiftOrders(Map<String, Object> params);
    boolean updatePosition(int taskNo, String taskStatus, int taskOrder);
    boolean update(TaskDto taskDto);
    boolean delete(int taskNo);
}