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
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.dto.ProjectExpectedResultDto;
import com.kh.finalprj.service.ProjectExpectedResultService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.project.ProjectExpectedResultRequestVO;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "프로젝트 기대결과 API")
@CommonsApiResponse

@RestController
@RequestMapping("/api/project/{projectNo}")
public class ProjectExpectedResultController {
	@Autowired
	private ProjectExpectedResultService projectExpectedResultService;

	//프로젝트 예상 결과 목록
	@ApiResponse(responseCode = "200",description = "프로젝트 기대결과 목록 조회")
	@GetMapping(value = "/result",produces = "application/json")
	public List<ProjectExpectedResultDto> resultList(
			@PathVariable int projectNo,
			@CurrentUser TokenParseResponseVO parseVO
			){
		int empNo = parseVO.getEmpNo();
		
		return projectExpectedResultService.resultList(projectNo,empNo);
	}
	
	//프로젝트 예상 결과 등록
	@ApiResponse(responseCode = "200",description = "프로젝트 기대결과 등록 성공")
	@PostMapping(value = "/result",produces = "application/json")
	public void resultadd(
			@PathVariable int projectNo,
			@RequestBody ProjectExpectedResultRequestVO requestVO,
			@CurrentUser TokenParseResponseVO parseVO
	){
		int empNo = parseVO.getEmpNo();
		
		projectExpectedResultService.resultAdd(projectNo,requestVO,empNo);
	}
	
	//프로젝트 기대결과 수정
	@ApiResponse(responseCode = "200",description = "프로젝트 기대결과 수정 성공")
	@PutMapping(value = "/result/{projectResultNo}",produces = "application/json")
	public void resultUpdate(
			@PathVariable int projectNo,
			@PathVariable int projectResultNo,
			@RequestBody ProjectExpectedResultRequestVO requestVO,
			@CurrentUser TokenParseResponseVO parseVO
	) {
		int empNo = parseVO.getEmpNo();
		
		projectExpectedResultService.resultUpdate(projectNo, projectResultNo, requestVO, empNo);
	}
	
	//프로젝트 기대결과 삭제
	@ApiResponse(responseCode = "200",description = "프로젝트 기대결과 삭제 성공")
	@DeleteMapping(value = "/result/{projectResultNo}",produces = "application/json")
	public void resultDelete(
			@PathVariable int projectNo,
			@PathVariable int projectResultNo,
			@CurrentUser TokenParseResponseVO parseVO
			
	) {
		int empNo = parseVO.getEmpNo();
		
		projectExpectedResultService.resultDelete(projectNo, projectResultNo, empNo);
	}
	
}
