package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.dto.ProjectExpectedResultDto;
import com.kh.finalprj.service.ProjectExpectedResultService;
import com.kh.finalprj.service.ProjectService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "프로젝트 기대결과 API")
@CommonsApiResponse

@RestController
@RequestMapping("/api/project/{projectNo}/result")
public class ProjectExpectedResultController {
	@Autowired
	private ProjectExpectedResultService projectExpectedResultService;

	//프로젝트 예상 결과 목록
	@ApiResponse(responseCode = "200",description = "프로젝트 기대결과 목록 조회")
	@GetMapping(value = "/",produces = "application/json")
	public List<ProjectExpectedResultDto> resultList(
			@PathVariable int projectNo,
			@CurrentUser TokenParseResponseVO parseVO
			){
		int empNo = parseVO.getEmpNo();
		
		return projectExpectedResultService.resultList(projectNo,empNo);
	}
	
	//프로젝트 예상 결과 등록
//	@ApiResponse(responseCode = "200",description = "프로젝트 기대결과 목록 조회")
//	@PostMapping(value = "/",produces = "application/json")
//	public ProjectExpectedResultDto add(
//			@PathVariable int projectNo,
//			@CurrentUser TokenParseResponseVO parseVO
//	){
//		int empNo = parseVO.getEmpNo();
//		
//		return projectExpectedResultService.resultAdd(projectNo,empNo);
//	}
//	
}
