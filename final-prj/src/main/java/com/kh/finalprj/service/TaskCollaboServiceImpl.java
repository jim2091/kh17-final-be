package com.kh.finalprj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dao.TaskCollaboDao;
import com.kh.finalprj.dao.TaskDao;
import com.kh.finalprj.dto.NotificationDto;
import com.kh.finalprj.dto.ProjectMemberDto;
import com.kh.finalprj.dto.TaskCollaboDto;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;

@Service
public class TaskCollaboServiceImpl implements TaskCollaboService {

	@Autowired
	private TaskCollaboDao taskCollaboDao;

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private ProjectMemberDao projectMemberDao;

	@Autowired
	private NotificationService notificationService;

	// 알림 발송 헬퍼 (TaskDetailResponseVO 참조)
	private void sendCollabNotification(TaskDetailResponseVO task, int projectMemberNo) {
		if (task == null || projectMemberNo <= 0) return;

		ProjectMemberDto collabMember = projectMemberDao.findMember2(projectMemberNo);
		if (collabMember != null && collabMember.getEmpNo() > 0) {
			notificationService.send(NotificationDto.builder()
					.notificationReceiver(collabMember.getEmpNo())
					.projectNo(task.getProjectNo())
					.notificationType("TASK_COLLAB_ADDED")
					.notificationTarget(task.getTaskNo())
					.notificationUrl("/projects/" + task.getProjectNo() + "/kanban?taskNo=" + task.getTaskNo())
					.notificationContent("'" + task.getTaskTitle() + "' 업무의 협업자로 지정되었습니다.")
					.build());
		}
	}

	// 1. 단건 협업자 등록
	@Override
	@Transactional
	public void add(int taskNo, int projectMemberNo) {
		taskCollaboDao.add(taskNo, projectMemberNo);

		TaskDetailResponseVO task = taskDao.selectOne(taskNo);
		sendCollabNotification(task, projectMemberNo);
	}

	// 2. 다건 협업자 일괄 등록
	@Override
	@Transactional
	public void addList(int taskNo, List<Integer> projectMemberNos) {
		if (projectMemberNos == null || projectMemberNos.isEmpty())
			return;

		TaskDetailResponseVO task = taskDao.selectOne(taskNo);

		for (Integer projectMemberNo : projectMemberNos) {
			if (projectMemberNo != null && projectMemberNo > 0) {
				taskCollaboDao.add(taskNo, projectMemberNo);
				sendCollabNotification(task, projectMemberNo);
			}
		}
	}

	// 3. 특정 업무의 협업자 목록 조회
	@Override
	@Transactional(readOnly = true)
	public List<TaskCollaboDto> selectByTaskNo(int taskNo) {
		return taskCollaboDao.selectByTaskNo(taskNo);
	}

	// 4. 협업자 전체 교체
	@Override
	@Transactional
	public void replaceCollaborators(int taskNo, List<Integer> newProjectMemberNos) {
		taskCollaboDao.deleteByTaskNo(taskNo);

		if (newProjectMemberNos != null && !newProjectMemberNos.isEmpty()) {
			TaskDetailResponseVO task = taskDao.selectOne(taskNo);

			for (Integer projectMemberNo : newProjectMemberNos) {
				if (projectMemberNo != null && projectMemberNo > 0) {
					taskCollaboDao.add(taskNo, projectMemberNo);
					sendCollabNotification(task, projectMemberNo);
				}
			}
		}
	}

	// 5. 특정 협업자 1명만 삭제
	@Override
	@Transactional
	public boolean deleteOne(int taskNo, int projectMemberNo) {
		return taskCollaboDao.deleteOne(taskNo, projectMemberNo);
	}
}