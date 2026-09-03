package com.kh.finalprj.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.finalprj.vo.task.TaskFileResponseVO;

public interface TaskFileService {

	// 업무 본체 첨부파일 업로드
	TaskFileResponseVO uploadTaskFile(int taskNo, int projectNo, String uploader, MultipartFile file) throws IOException;

	// 업무 첨부파일 목록 조회
	List<TaskFileResponseVO> getTaskFiles(int taskNo);

	// 업무 첨부파일 삭제
	boolean removeTaskFile(int taskNo, int attachNo, String uploader);

	// 댓글 첨부파일 업로드
	TaskFileResponseVO uploadCommentFile(int taskCommentNo, int projectNo, String uploader, MultipartFile file) throws IOException;

	// 댓글 첨부파일 목록 조회
	List<TaskFileResponseVO> getCommentFiles(int taskCommentNo);

	// 댓글 첨부파일 삭제
	boolean removeCommentFile(int taskCommentNo, int attachNo, String uploader);

}