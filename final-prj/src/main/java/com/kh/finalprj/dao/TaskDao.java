package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.TaskDto;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;

public interface TaskDao {
	int sequence();
	int add(TaskDto taskDto);
	TaskDetailResponseVO selectOne(int taskNo);
	List<TaskDto> selectByProjectNo(int projectNo);
	boolean update(TaskDto taskDto);
	boolean updatePosition(int taskNo, String columnStatus, int position);
	boolean delete(int taskNo);
}