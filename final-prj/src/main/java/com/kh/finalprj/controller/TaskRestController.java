package com.kh.finalprj.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dto.NotificationDto;
import com.kh.finalprj.dto.ProjectMemberDto;
import com.kh.finalprj.dto.TaskDto;
import com.kh.finalprj.service.NotificationService;
import com.kh.finalprj.service.TaskService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.task.TaskAddRequestVO;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;
import com.kh.finalprj.vo.task.TaskMoveRequestVO;
import com.kh.finalprj.vo.task.TaskMoveResponseVO;
import com.kh.finalprj.vo.task.TaskUpdateRequestVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "업무 API")
@CommonsApiResponse
@RestController
@RequestMapping("/api/task")
public class TaskRestController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private ProjectMemberDao projectMemberDao;

	// 1. 업무 생성 (내부 TaskServiceImpl.add에서 주 담당자 및 협업자 알림 처리 완료됨)
	@Operation(summary = "신규 업무 등록")
	@ApiResponse(responseCode = "200", description = "업무 생성 성공")
	@PostMapping(value = "/", produces = "application/json")
	public int add(@RequestBody TaskAddRequestVO requestVO, @CurrentUser TokenParseResponseVO parseVO) {

		int empNo = (parseVO != null) ? parseVO.getEmpNo() : 0;
		int generatedTaskNo = taskService.add(requestVO, requestVO.getCollaboratorMemberNos(), empNo);

		if (generatedTaskNo > 0) {
			simpMessagingTemplate.convertAndSend("/public/projects/" + requestVO.getProjectNo() + "/kanban",
					Map.of("eventType", "TASK_CREATED", "projectNo", requestVO.getProjectNo(), "taskNo",
							generatedTaskNo, "senderEmpNo", empNo));
		}

		return generatedTaskNo;
	}

	// 2. 전체 업무 리스트 조회
	@Operation(summary = "프로젝트 전체 업무 리스트 조회")
	@ApiResponse(responseCode = "200", description = "전체 리스트 조회 성공")
	@GetMapping(value = "/list/{projectNo}", produces = "application/json")
	public List<TaskDto> listByProject(@PathVariable int projectNo) {
		return taskService.selectByProjectNo(projectNo);
	}

	// 3. 업무 단건 상세 조회
	@Operation(summary = "업무 단건 상세 조회")
	@ApiResponse(responseCode = "200", description = "업무 조회 성공")
	@GetMapping(value = "/{taskNo}", produces = "application/json")
	public TaskDetailResponseVO find(@PathVariable int taskNo) {
		return taskService.selectOne(taskNo);
	}

	// 4. 칸반 보드 3단 분류 조회
	@Operation(summary = "칸반 보드 3단 분류 조회")
	@ApiResponse(responseCode = "200", description = "칸반 보드 조회 성공")
	@GetMapping(value = "/kanban/{projectNo}", produces = "application/json")
	public TaskMoveResponseVO findKanbanBoard(@PathVariable int projectNo) {
		return taskService.selectKanbanBoard(projectNo);
	}

	// 5. 업무 삭제 (Soft Delete)
	@Operation(summary = "업무 삭제")
	@ApiResponse(responseCode = "200", description = "업무 삭제 성공")
	@DeleteMapping(value = "/{taskNo}", produces = "application/json")
	public boolean delete(@PathVariable int taskNo, @RequestParam(required = false, defaultValue = "0") int projectNo,
			@CurrentUser TokenParseResponseVO parseVO) {

		int senderEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 0;
		boolean result = taskService.delete(taskNo);

		if (result && projectNo > 0) {
			simpMessagingTemplate.convertAndSend("/public/projects/" + projectNo + "/kanban", Map.of("eventType",
					"TASK_DELETED", "projectNo", projectNo, "taskNo", taskNo, "senderEmpNo", senderEmpNo));
		}

		return result;
	}

	// 6. 삭제된 업무 목록 조회 (휴지통)
	@Operation(summary = "휴지통 업무 목록 조회")
	@ApiResponse(responseCode = "200", description = "휴지통 목록 조회 성공")
	@GetMapping(value = "/deleted/{projectNo}", produces = "application/json")
	public List<TaskDto> getDeletedTasks(@PathVariable int projectNo) {
		return taskService.selectDeletedByProjectNo(projectNo);
	}

	// 7. 삭제된 업무 복구 (휴지통에서 꺼내오기)
	@Operation(summary = "삭제된 업무 복구")
	@ApiResponse(responseCode = "200", description = "업무 복구 성공")
	@PatchMapping(value = "/{taskNo}/restore", produces = "application/json")
	public boolean restore(@PathVariable int taskNo, @RequestParam(required = false, defaultValue = "0") int projectNo,
			@CurrentUser TokenParseResponseVO parseVO) {

		int senderEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 0;
		boolean result = taskService.restore(taskNo);

		if (result && projectNo > 0) {
			simpMessagingTemplate.convertAndSend("/public/projects/" + projectNo + "/kanban", Map.of("eventType",
					"TASK_RESTORED", "projectNo", projectNo, "taskNo", taskNo, "senderEmpNo", senderEmpNo));
		}

		return result;
	}

	// 8. 업무 내용 및 협업자 수정 (중복 호출 제거 완료)
	@Operation(summary = "업무 수정")
	@ApiResponse(responseCode = "200", description = "업무 수정 성공")
	@PutMapping(value = "/", produces = "application/json")
	public boolean update(@RequestBody TaskUpdateRequestVO updateVO, @CurrentUser TokenParseResponseVO parseVO) {

		int senderEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 0;
		TaskDetailResponseVO beforeTask = taskService.selectOne(updateVO.getTaskNo());

		// 서비스 계층에서 기본 정보 및 협업자 교체를 단일 트랜잭션으로 안전하게 처리
		boolean result = taskService.update(updateVO);

		if (result && updateVO.getProjectNo() > 0) {
			// 칸반 실시간 브로드캐스트
			simpMessagingTemplate.convertAndSend("/public/projects/" + updateVO.getProjectNo() + "/kanban",
					Map.of("eventType", "TASK_UPDATED", "projectNo", updateVO.getProjectNo(), "taskNo",
							updateVO.getTaskNo(), "senderEmpNo", senderEmpNo));

			// 담당자 변경 알림 발송
			Integer currentAssignee = updateVO.getAssignedMemberNo();
			Integer beforeAssignee = (beforeTask != null) ? beforeTask.getAssignedMemberNo() : null;

			if (currentAssignee != null && currentAssignee > 0 && !currentAssignee.equals(beforeAssignee)) {
				ProjectMemberDto assignedMember = projectMemberDao.findMember(currentAssignee);
				if (assignedMember != null && assignedMember.getEmpNo() != senderEmpNo) {
					notificationService.send(NotificationDto.builder()
							.notificationReceiver(assignedMember.getEmpNo())
							.projectNo(updateVO.getProjectNo())
							.notificationType("TASK_ASSIGNED")
							.notificationTarget(updateVO.getTaskNo())
							.notificationUrl("/projects/" + updateVO.getProjectNo() + "/kanban?taskNo=" + updateVO.getTaskNo())
							.notificationContent("'" + updateVO.getTaskTitle() + "' 업무의 담당자로 배정되었습니다.")
							.build());
				}
			}
		}

		return result;
	}

	// 9. 칸반 카드 드래그 이동 (DONE 완료 시 최초 작성자에게 완료 알림 발송)
	@Operation(summary = "칸반 이동")
	@ApiResponse(responseCode = "200", description = "칸반 이동 성공")
	@PatchMapping(value = "/move", produces = "application/json")
	public boolean moveTask(@RequestBody TaskMoveRequestVO moveVO, @CurrentUser TokenParseResponseVO parseVO) {

		int senderEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 0;
		boolean result = taskService.moveTask(moveVO);

		if (result) {
			simpMessagingTemplate.convertAndSend("/public/projects/" + moveVO.getProjectNo() + "/kanban",
					Map.of("eventType", "TASK_MOVED", "taskNo", moveVO.getTaskNo(), "nextStatus",
							moveVO.getTargetStatus(), "newOrder", moveVO.getNewOrder(), "senderEmpNo", senderEmpNo));

			if ("DONE".equalsIgnoreCase(moveVO.getTargetStatus())) {
				TaskDetailResponseVO currentTask = taskService.selectOne(moveVO.getTaskNo());

				if (currentTask != null && currentTask.getTaskWriterNo() > 0) {
					ProjectMemberDto writerMember = projectMemberDao.findMember(currentTask.getTaskWriterNo());
					if (writerMember != null && writerMember.getEmpNo() != senderEmpNo) {
						notificationService.send(NotificationDto.builder()
								.notificationReceiver(writerMember.getEmpNo())
								.projectNo(moveVO.getProjectNo())
								.notificationType("TASK_COMPLETED")
								.notificationTarget(moveVO.getTaskNo())
								.notificationUrl("/projects/" + moveVO.getProjectNo() + "/kanban?taskNo=" + moveVO.getTaskNo())
								.notificationContent("'" + currentTask.getTaskTitle() + "' 업무가 완료 처리되었습니다.")
								.build());
					}
				}
			}
		}
		return result;
	}
}