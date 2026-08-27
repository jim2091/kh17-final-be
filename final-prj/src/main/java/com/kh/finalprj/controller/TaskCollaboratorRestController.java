package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.dto.TaskCollaboDto;
import com.kh.finalprj.service.TaskCollaboService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "업무 협업자 API")
@CommonsApiResponse
@RestController
@RequestMapping("/api/task-collabo")
public class TaskCollaboratorRestController {

	@Autowired
	private TaskCollaboService taskCollaboService;

	// [1] 특정 업무의 협업자 목록 조회
	@ApiResponse(responseCode = "200", description = "협업자 목록 조회 성공")
	@GetMapping(value = "/{taskNo}", produces = "application/json")
	public List<TaskCollaboDto> list(@PathVariable int taskNo) {
		return taskCollaboService.selectByTaskNo(taskNo);
	}

	// [2] 협업자 단건 등록
	@ApiResponse(responseCode = "200", description = "협업자 등록 성공")
	@PostMapping(value = "/{taskNo}/{projectMemberNo}", produces = "application/json")
	public void add(
			@PathVariable int taskNo, 
			@PathVariable int projectMemberNo) {
		taskCollaboService.add(taskNo, projectMemberNo);
	}

	// [3] 협업자 다건 일괄 등록 (배열 전달)
	@ApiResponse(responseCode = "200", description = "협업자 일괄 등록 성공")
	@PostMapping(value = "/{taskNo}/batch", produces = "application/json")
	public void addList(
			@PathVariable int taskNo, 
			@RequestBody List<Integer> projectMemberNos) {
		taskCollaboService.addList(taskNo, projectMemberNos);
	}

	// [4] 협업자 전체 교체 (상세 드로어 모달에서 멀티 셀렉트 수정 시 기존 목록을 비우고 새 목록으로 동기화)
	@ApiResponse(responseCode = "200", description = "협업자 교체 성공")
	@PutMapping(value = "/{taskNo}", produces = "application/json")
	public void replaceCollaborators(
			@PathVariable int taskNo, 
			@RequestBody List<Integer> newProjectMemberNos) {
		taskCollaboService.replaceCollaborators(taskNo, newProjectMemberNos);
	}

	// [5] 특정 협업자 1명 단건 삭제
	@ApiResponse(responseCode = "200", description = "협업자 삭제 성공")
	@DeleteMapping(value = "/{taskNo}/{projectMemberNo}", produces = "application/json")
	public boolean deleteOne(
			@PathVariable int taskNo, 
			@PathVariable int projectMemberNo) {
		return taskCollaboService.deleteOne(taskNo, projectMemberNo);
	}
}