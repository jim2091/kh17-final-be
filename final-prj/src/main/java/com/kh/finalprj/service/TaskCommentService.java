package com.kh.finalprj.service;

import java.util.List;
import com.kh.finalprj.dto.TaskCommentDto;
import com.kh.finalprj.vo.task.TaskCommentDetailResponseVO;

public interface TaskCommentService {
	int add(TaskCommentDto taskCommentDto, int loginEmpNo); 
	TaskCommentDetailResponseVO selectOne(int taskCommentNo);
	List<TaskCommentDto> findComments(int taskNo);
	boolean update(TaskCommentDto taskCommentDto);
	boolean delete(int taskCommentNo);
}