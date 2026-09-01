package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.TaskCommentDto;
import com.kh.finalprj.vo.task.TaskCommentDetailResponseVO;

public interface TaskCommentsDao {
	int sequence();
	int add(TaskCommentDto taskCommentDto);
	TaskCommentDetailResponseVO selectOne(int taskCommentNo);
	List<TaskCommentDto> findComments(int taskNo);
	boolean update(TaskCommentDto taskCommentDto);
	boolean delete(int taskCommentNo);
}
