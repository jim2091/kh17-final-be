package com.kh.finalprj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.service.TaskService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.task.TaskAddRequestVO;

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

	// 업무 생성
	@ApiResponse(responseCode = "200", description = "업무 생성 성공")
	@PostMapping(value = "/", produces = "application/json")
	public int add(
			@RequestBody TaskAddRequestVO requestVO, 
			@CurrentUser TokenParseResponseVO parseVO) {
		
		int empNo = parseVO.getEmpNo();
		// VO 내부의 collaboratorMemberNos 리스트를 서비스에 전달
		return taskService.add(requestVO, requestVO.getCollaboratorMemberNos(), empNo);
	}
}