package com.kh.finalprj.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dao.TaskCollaboDao;
import com.kh.finalprj.dao.TaskDao;
import com.kh.finalprj.dao.TaskFileDao;
import com.kh.finalprj.dto.NotificationDto;
import com.kh.finalprj.dto.ProjectMemberDto;
import com.kh.finalprj.dto.TaskDto;
import com.kh.finalprj.error.GetOutException;
import com.kh.finalprj.vo.task.TaskAddRequestVO;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;
import com.kh.finalprj.vo.task.TaskMoveRequestVO;
import com.kh.finalprj.vo.task.TaskMoveResponseVO;
import com.kh.finalprj.vo.task.TaskUpdateRequestVO;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private TaskFileDao taskFileDao;

	@Autowired
	private AttachService attachService;

	@Autowired
	private TaskCollaboDao taskCollaboDao;

	@Autowired
	private ProjectMemberDao projectMemberDao;

	@Autowired
	private NotificationService notificationService;

	// String/Object 날짜 값을 안전하게 Timestamp로 변환하는 내부 헬퍼 메서드
	private Timestamp parseTimestamp(Object dateObj) {
		if (dateObj == null) return null;
		if (dateObj instanceof Timestamp) return (Timestamp) dateObj;

		String str = String.valueOf(dateObj).trim();
		if (str.isEmpty() || "null".equalsIgnoreCase(str)) return null;

		// ISO-8601 (T 구분자) 처리
		str = str.replace("T", " ");

		// yyyy-MM-dd 형태(10자)인 경우 시분초 보정
		if (str.length() == 10) {
			str += " 00:00:00";
		}

		// yyyy-MM-dd HH:mm 형태(16자)인 경우 초 보정
		if (str.length() == 16) {
			str += ":00";
		}

		try {
			return Timestamp.valueOf(str);
		} catch (Exception e) {
			return null;
		}
	}

	// 1. 신규 업무 등록 및 알림 발송
	@Override
	@Transactional
	public int add(TaskAddRequestVO requestVO, List<Integer> collaboratorMemberNos, int empNo) {
		Integer projectMemberNo = projectMemberDao.findProjectMemberNo(requestVO.getProjectNo(), empNo);

		if (projectMemberNo == null) {
			throw new GetOutException("해당 프로젝트에 참여 중인 멤버가 아닙니다.");
		}

		int generatedTaskNo = taskDao.sequence();

		// 주 담당자 번호 null 및 0 이하 값 방어
		Integer assignedMemberNo = requestVO.getAssignedMemberNo();
		if (assignedMemberNo != null && assignedMemberNo <= 0) {
			assignedMemberNo = null;
		}

		// 날짜 안전 변환
		Timestamp startTs = parseTimestamp(requestVO.getTaskStart());
		Timestamp endTs = parseTimestamp(requestVO.getTaskEnd());

		TaskDto taskDto = TaskDto.builder()
				.taskNo(generatedTaskNo)
				.projectNo(requestVO.getProjectNo())
				.taskTitle(requestVO.getTaskTitle())
				.taskContent(requestVO.getTaskContent())
				.taskWriterNo(projectMemberNo)
				.assignedMemberNo(assignedMemberNo)
				.taskStatus(requestVO.getTaskStatus() != null ? requestVO.getTaskStatus() : "TODO")
				.taskOrder(1)
				.taskStart(startTs)
				.taskEnd(endTs)
				.taskCategory(requestVO.getTaskCategory())
				.taskPriority(requestVO.getTaskPriority() != null ? requestVO.getTaskPriority() : "보통")
				.build();

		taskDao.add(taskDto);

		// 주 담당자 배정 알림 발송
		if (assignedMemberNo != null && assignedMemberNo > 0) {
			ProjectMemberDto assignedMember = projectMemberDao.findMember(assignedMemberNo);
			if (assignedMember != null && assignedMember.getEmpNo() != empNo) {
				notificationService.send(NotificationDto.builder()
						.notificationReceiver(assignedMember.getEmpNo())
						.projectNo(requestVO.getProjectNo())
						.notificationType("TASK_ASSIGNED")
						.notificationTarget(generatedTaskNo)
						.notificationUrl("/projects/" + requestVO.getProjectNo() + "/kanban?taskNo=" + generatedTaskNo)
						.notificationContent("'" + requestVO.getTaskTitle() + "' 업무의 담당자로 배정되었습니다.")
						.build());
			}
		}

		// 협업자 목록 등록 및 알림 발송
		if (collaboratorMemberNos != null && !collaboratorMemberNos.isEmpty()) {
			for (Integer memberNo : collaboratorMemberNos) {
				if (memberNo != null && memberNo > 0) {
					if (assignedMemberNo == null || !assignedMemberNo.equals(memberNo)) {
						taskCollaboDao.add(generatedTaskNo, memberNo);

						ProjectMemberDto collabMember = projectMemberDao.findMember(memberNo);
						if (collabMember != null && collabMember.getEmpNo() != empNo) {
							notificationService.send(NotificationDto.builder()
									.notificationReceiver(collabMember.getEmpNo())
									.projectNo(requestVO.getProjectNo())
									.notificationType("TASK_COLLAB_ADDED")
									.notificationTarget(generatedTaskNo)
									.notificationUrl("/projects/" + requestVO.getProjectNo() + "/kanban?taskNo=" + generatedTaskNo)
									.notificationContent("'" + requestVO.getTaskTitle() + "' 업무의 협업자로 등록되었습니다.")
									.build());
						}
					}
				}
			}
		}

		return generatedTaskNo;
	}

	// 2. 단건 상세 조회 (협업자 목록 결합)
	@Override
	@Transactional(readOnly = true)
	public TaskDetailResponseVO selectOne(int taskNo) {
		TaskDetailResponseVO detail = taskDao.selectOne(taskNo);
		if (detail != null) {
			detail.setCollaborators(taskCollaboDao.selectByTaskNo(taskNo));
		}
		return detail;
	}

	// 3. 프로젝트별 업무 단순 리스트 조회
	@Override
	@Transactional(readOnly = true)
	public List<TaskDto> selectByProjectNo(int projectNo) {
		return taskDao.selectByProjectNo(projectNo);
	}

	// 4. 칸반 보드 3단 분류 조회
	@Override
	@Transactional(readOnly = true)
	public TaskMoveResponseVO selectKanbanBoard(int projectNo) {
		List<TaskDto> allTasks = taskDao.selectByProjectNo(projectNo);
		return TaskMoveResponseVO.builder()
				.todoList(allTasks.stream().filter(t -> "TODO".equals(t.getTaskStatus())).toList())
				.inProgressList(allTasks.stream().filter(t -> "IN_PROGRESS".equals(t.getTaskStatus())).toList())
				.doneList(allTasks.stream().filter(t -> "DONE".equals(t.getTaskStatus())).toList())
				.build();
	}

	// 5. 칸반 카드 드래그 이동
	@Override
	@Transactional
	public boolean moveTask(TaskMoveRequestVO moveVO) {
		Map<String, Object> params = new HashMap<>();
		params.put("projectNo", moveVO.getProjectNo());
		params.put("taskStatus", moveVO.getTargetStatus());
		params.put("newOrder", moveVO.getNewOrder());
		params.put("taskNo", moveVO.getTaskNo());
		taskDao.shiftOrders(params);

		return taskDao.updatePosition(moveVO.getTaskNo(), moveVO.getTargetStatus(), moveVO.getNewOrder());
	}

	// 6. 업무 기본 정보 단독 수정
	@Override
	@Transactional
	public boolean update(TaskDto taskDto) {
		return taskDao.update(taskDto);
	}

	// 7. 업무 내용 및 협업자 목록 동시 수정 (드로어 편집 전용)
	@Override
	@Transactional
	public boolean update(TaskUpdateRequestVO updateVO) {
		// 기존 업무 정보 조회 (taskOrder 유지)
		TaskDetailResponseVO original = taskDao.selectOne(updateVO.getTaskNo());
		int currentOrder = (original != null) ? original.getTaskOrder() : 1;

		// 주 담당자 번호 null 방어
		Integer assignedMemberNo = updateVO.getAssignedMemberNo();
		if (assignedMemberNo != null && assignedMemberNo <= 0) {
			assignedMemberNo = null;
		}

		// String/Object 날짜를 Timestamp로 안전 변환
		Timestamp startTs = parseTimestamp(updateVO.getTaskStart());
		Timestamp endTs = parseTimestamp(updateVO.getTaskEnd());

		TaskDto taskDto = TaskDto.builder()
				.taskNo(updateVO.getTaskNo())
				.projectNo(updateVO.getProjectNo())
				.taskTitle(updateVO.getTaskTitle())
				.taskContent(updateVO.getTaskContent())
				.assignedMemberNo(assignedMemberNo)
				.taskStatus(updateVO.getTaskStatus() != null ? updateVO.getTaskStatus() : "TODO")
				.taskOrder(currentOrder)
				.taskPriority(updateVO.getTaskPriority() != null ? updateVO.getTaskPriority() : "보통")
				.taskCategory(updateVO.getTaskCategory())
				.taskStart(startTs)
				.taskEnd(endTs)
				.build();

		boolean result = taskDao.update(taskDto);

		// 기존 협업자 전체 삭제 후 재등록
		taskCollaboDao.deleteByTaskNo(updateVO.getTaskNo());

		List<Integer> collabList = updateVO.getCollaboratorMemberNos();
		if (collabList != null && !collabList.isEmpty()) {
			for (Integer memberNo : collabList) {
				if (memberNo != null && memberNo > 0) {
					if (assignedMemberNo == null || !assignedMemberNo.equals(memberNo)) {
						taskCollaboDao.add(updateVO.getTaskNo(), memberNo);
					}
				}
			}
		}

		return result;
	}

	// 8. 업무 삭제
	@Override
	@Transactional
	public boolean delete(int taskNo) {
		return taskDao.delete(taskNo);
	}

	// 9. 업무 복원
	@Override
	@Transactional
	public boolean restore(int taskNo) {
		return taskDao.restore(taskNo);
	}

	// 10. 삭제된 업무 목록 조회
	@Override
	@Transactional(readOnly = true)
	public List<TaskDto> selectDeletedByProjectNo(int projectNo) {
		return taskDao.selectDeletedByProjectNo(projectNo);
	}

}