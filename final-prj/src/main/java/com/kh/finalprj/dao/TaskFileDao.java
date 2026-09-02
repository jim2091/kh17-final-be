package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.TaskCommentFileDto;
import com.kh.finalprj.dto.TaskFileDto;
import com.kh.finalprj.vo.task.TaskFileResponseVO;

public interface TaskFileDao {
	
	//업무 본체 첨부파일
	void addTaskFile(TaskFileDto taskFileDto);
	List<TaskFileResponseVO> selectFilesByTaskNo(int taskNo);
	boolean deleteTaskFile(int taskNo, int attachNo);
	
	//댓글 첨부파일
	void addCommentFile(TaskCommentFileDto taskCommentFileDto);
	List<TaskFileResponseVO>selectFilesByCommentNo(int taskCommentNo);
	boolean deleteCommentFile(int taskCommentNo, int attachNo);
}
