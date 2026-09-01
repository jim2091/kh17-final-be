package com.kh.finalprj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dao.TaskCommentsDao;
import com.kh.finalprj.dao.TaskDao;
import com.kh.finalprj.dto.TaskCommentDto;
import com.kh.finalprj.vo.task.TaskCommentDetailResponseVO;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;

@Service
public class TaskCommentServiceImpl implements TaskCommentService {

	@Autowired
	private TaskCommentsDao taskCommentsDao;

	@Autowired
	private ProjectMemberDao projectMemberDao; 

	@Autowired
	private TaskDao taskDao; 

	@Override
	@Transactional
	public int add(TaskCommentDto taskCommentDto) {
		if (taskCommentDto.getProjectMemberNo() <= 0 && taskCommentDto.getEmpNo() > 0) {
			TaskDetailResponseVO task = taskDao.selectOne(taskCommentDto.getTaskNo());
			if (task != null) {
				Integer validMemberNo = projectMemberDao.findProjectMemberNo(task.getProjectNo(), taskCommentDto.getEmpNo());
				if (validMemberNo != null) {
					taskCommentDto.setProjectMemberNo(validMemberNo);
				}
			}
		}

		int generatedCommentNo = taskCommentsDao.sequence();
		taskCommentDto.setTaskCommentNo(generatedCommentNo);

		taskCommentsDao.add(taskCommentDto);
		
		return generatedCommentNo;
	}

	@Override
	@Transactional(readOnly = true)
	public TaskCommentDetailResponseVO selectOne(int taskCommentNo) {
		return taskCommentsDao.selectOne(taskCommentNo);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TaskCommentDto> findComments(int taskNo) {
		return taskCommentsDao.findComments(taskNo);
	}

	@Override
	@Transactional
	public boolean update(TaskCommentDto taskCommentDto) {
		return taskCommentsDao.update(taskCommentDto);
	}

	@Override
	@Transactional
	public boolean delete(int taskCommentNo) {
		return taskCommentsDao.delete(taskCommentNo);
	}
}