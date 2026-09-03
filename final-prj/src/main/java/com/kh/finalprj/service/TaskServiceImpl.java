package com.kh.finalprj.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dao.TaskCollaboDao;
import com.kh.finalprj.dao.TaskDao;
import com.kh.finalprj.dto.TaskDto;
import com.kh.finalprj.error.GetOutException;
import com.kh.finalprj.vo.task.TaskAddRequestVO;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;
import com.kh.finalprj.vo.task.TaskMoveRequestVO;
import com.kh.finalprj.vo.task.TaskMoveResponseVO;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private TaskCollaboDao taskCollaboDao;

	@Autowired
	private ProjectMemberDao projectMemberDao;

	@Override
	@Transactional
	public int add(TaskAddRequestVO requestVO, List<Integer> collaboratorMemberNos, int empNo) {
		Integer projectMemberNo = projectMemberDao.findProjectMemberNo(requestVO.getProjectNo(), empNo);

		if (projectMemberNo == null) {
			throw new GetOutException("해당 프로젝트에 참여 중인 멤버가 아닙니다.");
		}

		int generatedTaskNo = taskDao.sequence();

		TaskDto taskDto = TaskDto.builder().taskNo(generatedTaskNo).projectNo(requestVO.getProjectNo())
				.taskTitle(requestVO.getTaskTitle()).taskContent(requestVO.getTaskContent())
				.taskWriterNo(projectMemberNo) // 👈 project_member_no 주입[cite: 1, 2]
				.assignedMemberNo(requestVO.getAssignedMemberNo())
				.taskStatus(requestVO.getTaskStatus() != null ? requestVO.getTaskStatus() : "TODO").taskOrder(1)
				.taskStart(requestVO.getTaskStart()).taskEnd(requestVO.getTaskEnd())
				.taskCategory(requestVO.getTaskCategory())
				.taskPriority(requestVO.getTaskPriority() != null ? requestVO.getTaskPriority() : "보통")
				.build();

		taskDao.add(taskDto);

		// 협업자 목록 등록
		if (collaboratorMemberNos != null && !collaboratorMemberNos.isEmpty()) {
			for (Integer memberNo : collaboratorMemberNos) {
				if (memberNo != null) {
					taskCollaboDao.add(generatedTaskNo, memberNo);
				}
			}
		}

		return generatedTaskNo;
	}

	// 단건 상세 조회 (협업자 목록 결합)
	@Override
	@Transactional(readOnly = true)
	public TaskDetailResponseVO selectOne(int taskNo) {
		TaskDetailResponseVO detail = taskDao.selectOne(taskNo);
		if (detail != null) {
			detail.setCollaborators(taskCollaboDao.selectByTaskNo(taskNo));
		}
		return detail;
	}

	// 프로젝트별 업무 단순 리스트 조회 (구현 완료)
	@Override
	@Transactional(readOnly = true)
	public List<TaskDto> selectByProjectNo(int projectNo) {
		return taskDao.selectByProjectNo(projectNo);
	}

	// 칸반 보드 3단 분류 조회 (TODO, IN_PROGRESS, DONE)
	@Override
	@Transactional(readOnly = true)
	public TaskMoveResponseVO selectKanbanBoard(int projectNo) {
		List<TaskDto> allTasks = taskDao.selectByProjectNo(projectNo);
		return TaskMoveResponseVO.builder()
				.todoList(allTasks.stream().filter(t -> "TODO".equals(t.getTaskStatus())).toList())
				.inProgressList(allTasks.stream().filter(t -> "IN_PROGRESS".equals(t.getTaskStatus())).toList())
				.doneList(allTasks.stream().filter(t -> "DONE".equals(t.getTaskStatus())).toList()).build();
	}

	// 칸반 카드 드래그 이동 (순서 밀기 + 상태/순서 변경)
	@Override
	@Transactional
	public boolean moveTask(TaskMoveRequestVO moveVO) {
		// 타겟 컬럼 내 삽입될 위치(newOrder) 이상의 기존 카드들을 뒤로 1칸씩 밀기
		Map<String, Object> params = new HashMap<>();
		params.put("projectNo", moveVO.getProjectNo());
		params.put("taskStatus", moveVO.getTargetStatus());
		params.put("newOrder", moveVO.getNewOrder());
		params.put("taskNo", moveVO.getTaskNo());
		taskDao.shiftOrders(params);

		// 대상 카드의 상태 및 순서 최종 변경
		return taskDao.updatePosition(moveVO.getTaskNo(), moveVO.getTargetStatus(), moveVO.getNewOrder());
	}

	// 업무 내용 일반 수정
	@Override
	@Transactional
	public boolean update(TaskDto taskDto) {
		return taskDao.update(taskDto);
	}

	// 업무 삭제
	@Override
	@Transactional
	public boolean delete(int taskNo) {
		return taskDao.delete(taskNo);
	}
}