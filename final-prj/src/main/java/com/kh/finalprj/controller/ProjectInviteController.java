package com.kh.finalprj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.dto.ProjectInviteDto;
import com.kh.finalprj.service.ProjectInviteService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "프로젝트 초대 API")
@CommonsApiResponse
@RestController
@RequestMapping("/api/project")
public class ProjectInviteController {

	@Autowired
	private ProjectInviteService projectInviteService;
	
	//프로젝트 초대
	@ApiResponse(responseCode = "200", description = "초대 성공")
	@PostMapping(value = "/{projectNo}/invite/{receiverEmpNo}",produces = "application/json")
	public void invite(
			@PathVariable int projectNo,
			@CurrentUser TokenParseResponseVO parseVO,
			@PathVariable int receiverEmpNo
	) {
		projectInviteService.invite(projectNo,parseVO.getEmpNo(),receiverEmpNo);
	}
	
	//초대 수락
	@ApiResponse(responseCode = "200", description = "초대 수락 성공")
	@PatchMapping(value = "/invite/{projectInviteNo}/accept",produces = "application/json")
	public void accept(
			@PathVariable int projectInviteNo,
			@CurrentUser TokenParseResponseVO parseVO
	) {
		projectInviteService.accept(projectInviteNo, parseVO.getEmpNo());
	}
	
	//초대 거절
	@ApiResponse(responseCode = "200", description = "초대 거절 성공")
	@PatchMapping(value = "/invite/{projectInviteNo}/reject",produces = "application/json")
	public void reject(
			@PathVariable int projectInviteNo,
			@CurrentUser TokenParseResponseVO parseVO
	) {
		projectInviteService.reject(projectInviteNo, parseVO.getEmpNo());
	}
	
	//초대 거절,수락 했는지
	@ApiResponse(responseCode = "200", description = "초대 확인 성공")
	@GetMapping("/invite/{projectInviteNo}")
	public ProjectInviteDto inviteDetail(@PathVariable int projectInviteNo) {
		
		return projectInviteService.find(projectInviteNo);
	}
}
