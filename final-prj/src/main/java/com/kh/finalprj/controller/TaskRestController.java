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
import com.kh.finalprj.dto.TaskDto;
import com.kh.finalprj.service.TaskService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.task.TaskAddRequestVO;
import com.kh.finalprj.vo.task.TaskDetailResponseVO;
import com.kh.finalprj.vo.task.TaskMoveRequestVO;
import com.kh.finalprj.vo.task.TaskMoveResponseVO;

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

	// 1. 업무 생성 (생성 성공 시 실시간 브로드캐스트 전송)
	@Operation(summary = "신규 업무 등록")
	@ApiResponse(responseCode = "200", description = "업무 생성 성공")
	@PostMapping(value = "/", produces = "application/json")
	public int add(
			@RequestBody TaskAddRequestVO requestVO, 
			@CurrentUser TokenParseResponseVO parseVO) {

		int empNo = (parseVO != null) ? parseVO.getEmpNo() : 0;
		int generatedTaskNo = taskService.add(requestVO, requestVO.getCollaboratorMemberNos(), empNo);

		// 업무 생성 성공 시 프로젝트 전체 채널로 브로드캐스트
		if (generatedTaskNo > 0) {
			simpMessagingTemplate.convertAndSend(
				"/public/projects/" + requestVO.getProjectNo() + "/kanban",
				Map.of(
					"eventType", "TASK_CREATED",
					"projectNo", requestVO.getProjectNo(),
					"taskNo", generatedTaskNo,
					"senderEmpNo", empNo
				)
			);
		}

		return generatedTaskNo;
	}

	// 2. 전체 업무 조회 (테이블 목록 뷰 / 전체 조회용)
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

	// 4. 칸반보드용 3단 상세 조회
	@Operation(summary = "칸반 보드 3단 분류 조회")
	@ApiResponse(responseCode = "200", description = "칸반 보드 조회 성공")
	@GetMapping(value = "/kanban/{projectNo}", produces = "application/json")
	public TaskMoveResponseVO findKanbanBoard(@PathVariable int projectNo) {
		return taskService.selectKanbanBoard(projectNo);
	}

	// 5. 업무 삭제 (삭제 성공 시 실시간 브로드캐스트 전송)
	@Operation(summary = "업무 삭제")
	@ApiResponse(responseCode = "200", description = "업무 삭제 성공")
	@DeleteMapping(value = "/{taskNo}", produces = "application/json")
	public boolean delete(
			@PathVariable int taskNo,
			@RequestParam(required = false, defaultValue = "0") int projectNo,
			@CurrentUser TokenParseResponseVO parseVO) {
		
		int senderEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 0;
		boolean result = taskService.delete(taskNo);

		// 삭제 성공 시 실시간 전파
		if (result && projectNo > 0) {
			simpMessagingTemplate.convertAndSend(
				"/public/projects/" + projectNo + "/kanban",
				Map.of(
					"eventType", "TASK_DELETED",
					"projectNo", projectNo,
					"taskNo", taskNo,
					"senderEmpNo", senderEmpNo
				)
			);
		}

		return result;
	}

	// 6. 업무 상세 수정 (수정 성공 시 실시간 브로드캐스트 전송)
	@Operation(summary = "업무 상세 수정")
	@ApiResponse(responseCode = "200", description = "업무 수정 성공")
	@PutMapping(value = "/", produces = "application/json")
	public boolean update(
			@RequestBody TaskDto taskDto,
			@CurrentUser TokenParseResponseVO parseVO) {
		
		int senderEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 0;
		boolean result = taskService.update(taskDto);

		// 수정 성공 시 프로젝트 채널로 실시간 갱신 이벤트 전파
		if (result && taskDto.getProjectNo() > 0) {
			simpMessagingTemplate.convertAndSend(
				"/public/projects/" + taskDto.getProjectNo() + "/kanban",
				Map.of(
					"eventType", "TASK_UPDATED",
					"projectNo", taskDto.getProjectNo(),
					"taskNo", taskDto.getTaskNo(),
					"senderEmpNo", senderEmpNo
				)
			);
		}

		return result;
	}

	// 7. 칸반 카드 드래그 앤 드롭 이동
	@Operation(summary = "칸반 카드 위치 이동")
	@ApiResponse(responseCode = "200", description = "칸반 이동 성공")
	@PatchMapping(value = "/move", produces = "application/json")
	public boolean moveTask(
			@RequestBody TaskMoveRequestVO moveVO,
			@CurrentUser TokenParseResponseVO parseVO) {
		
		boolean result = taskService.moveTask(moveVO);

		if (result) {
			int senderEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 0;

			simpMessagingTemplate.convertAndSend(
				"/public/projects/" + moveVO.getProjectNo() + "/kanban",
				Map.of(
					"eventType", "TASK_MOVED",
					"taskNo", moveVO.getTaskNo(),
					"nextStatus", moveVO.getTargetStatus(),
					"newOrder", moveVO.getNewOrder(),
					"senderEmpNo", senderEmpNo 
				)
			);
		}
		return result;
	}
}