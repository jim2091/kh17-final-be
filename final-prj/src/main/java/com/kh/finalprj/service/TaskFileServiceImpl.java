package com.kh.finalprj.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.finalprj.dao.TaskFileDao;
import com.kh.finalprj.dto.TaskCommentFileDto;
import com.kh.finalprj.dto.TaskFileDto;
import com.kh.finalprj.vo.task.TaskFileResponseVO;

@Service
public class TaskFileServiceImpl implements TaskFileService {

	@Autowired
	private TaskFileDao taskFileDao;

	@Autowired
	private AttachService attachService;

	//업무 본체 첨부파일 업로드
	@Override
	@Transactional
	public TaskFileResponseVO uploadTaskFile(int taskNo, String fileRole, MultipartFile file) throws IOException {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("업로드할 파일이 없습니다.");
		}

		int attachNo = attachService.save(file);

		// task_file 매핑 테이블에 등록
		TaskFileDto taskFileDto = TaskFileDto.builder()
				.taskNo(taskNo)
				.attachNo(attachNo)
				.fileRole(fileRole != null && !fileRole.isBlank() ? fileRole : "REFERENCE")
				.build();
		taskFileDao.addTaskFile(taskFileDto);

		// 응답 VO 구성 반환
		return TaskFileResponseVO.builder()
				.attachNo(attachNo)
				.attachName(file.getOriginalFilename())
				.attachType(file.getContentType())
				.attachSize(file.getSize())
				.fileRole(taskFileDto.getFileRole())
				.build();
	}

	//업무 첨부파일 목록 조회
	@Override
	@Transactional(readOnly = true)
	public List<TaskFileResponseVO> getTaskFiles(int taskNo) {
		return taskFileDao.selectFilesByTaskNo(taskNo);
	}

	//업무 첨부파일 삭제
	@Override
	@Transactional
	public boolean removeTaskFile(int taskNo, int attachNo) {
		// task_file 매핑 삭제
		return taskFileDao.deleteTaskFile(taskNo, attachNo);
	}

	//댓글 첨부파일 업로드
	@Override
	@Transactional
	public TaskFileResponseVO uploadCommentFile(int taskCommentNo, String fileRole, MultipartFile file)
			throws IOException {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("업로드할 파일이 없습니다.");
		}

		int attachNo = attachService.save(file);

		TaskCommentFileDto commentFileDto = TaskCommentFileDto.builder()
				.taskCommentNo(taskCommentNo)
				.attachNo(attachNo)
				.fileRole(fileRole != null && !fileRole.isBlank() ? fileRole : "FEEDBACK")
				.build();
		taskFileDao.addCommentFile(commentFileDto);

		return TaskFileResponseVO.builder()
				.attachNo(attachNo)
				.attachName(file.getOriginalFilename())
				.attachType(file.getContentType())
				.attachSize(file.getSize())
				.fileRole(commentFileDto.getFileRole())
				.build();
	}

	//댓글 첨부파일 목록 조회
	@Override
	@Transactional(readOnly = true)
	public List<TaskFileResponseVO> getCommentFiles(int taskCommentNo) {
		return taskFileDao.selectFilesByCommentNo(taskCommentNo);
	}

	//댓글 첨부파일 삭제
	@Override
	@Transactional
	public boolean removeCommentFile(int taskCommentNo, int attachNo) {
		// task_comment_file 매핑 삭제
		return taskFileDao.deleteCommentFile(taskCommentNo, attachNo);
	}

}
