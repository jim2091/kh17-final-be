package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.service.ProjectRecordService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordAddRequestVO;
import com.kh.finalprj.vo.record.ProjectRecordAddResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordDetailResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordEditRequestVO;
import com.kh.finalprj.vo.record.ProjectRecordListResponseVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "프로젝트 record API")
@CommonsApiResponse
@RestController
@RequestMapping("/api/record")
public class ProjectRecordRestController {
	
	@Autowired
	private ProjectRecordService projectRecordService;
	
	//등록
	@Operation(summary = "프로젝트 record 등록")
	@ApiResponse(responseCode = "200", description = "record 등록 성공")
	@PostMapping("/project/{projectNo}")
	public ProjectRecordAddResponseVO add(
			@PathVariable int projectNo,
			@Valid @RequestBody ProjectRecordAddRequestVO request,
			@CurrentUser TokenParseResponseVO parseVO) {
		
		return projectRecordService.add(projectNo, parseVO.getEmpNo(), request);
	}
	
	@Operation(summary = "프로젝트 record 목록 조회")
	@ApiResponse(responseCode = "200", description = "record 목록 조회 성공")
	@GetMapping("/project/{projectNo}")
	public List<ProjectRecordListResponseVO> list(
			@PathVariable int projectNo,
			@CurrentUser TokenParseResponseVO parseVO) {
		
		return projectRecordService.list(projectNo, parseVO.getEmpNo());
	}
	
	@Operation(summary = "프로젝트 record 상세 조회")
	@ApiResponse(responseCode = "200", description = "record 상세 조회 성공")
	@GetMapping("/{projectRecordNo}")
	public ProjectRecordDetailResponseVO detail(
			@PathVariable int projectRecordNo,
			@CurrentUser TokenParseResponseVO parseVO) {
		
		return projectRecordService.detail(projectRecordNo, parseVO.getEmpNo());
	}
	
	@Operation(summary = "프로젝트 record 수정")
	@ApiResponse(responseCode = "200", description = "record 수정 성공")
	@PutMapping("/{projectRecordNo}")
	public void edit(
			@PathVariable int projectRecordNo,
			@RequestBody @Valid ProjectRecordEditRequestVO request,
			@CurrentUser TokenParseResponseVO parseVO) {
		
		projectRecordService.edit(projectRecordNo, parseVO.getEmpNo(), request);
	}
}
