package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.service.ProjectService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.project.ProjectCreateRequestVO;
import com.kh.finalprj.vo.project.ProjectListResponseVO;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "프로젝트 API")
@CommonsApiResponse

@RestController
@RequestMapping("/api/project")
public class ProjectController {

	@Autowired
	private ProjectService projectService;
	
	//프로젝트 생성 매핑
	@ApiResponse(responseCode = "200", description = "프로젝트 생성 성공")
	@PostMapping(value = "/", produces = "application/json")
	public int create(@RequestBody ProjectCreateRequestVO requestVO,
					@CurrentUser TokenParseResponseVO user) {

		//번호 추춯
		int empNo = user.getEmpNo();
		
		return projectService.create(requestVO,empNo);
	}
	
	//내 프로젝트 목록 매핑
	@ApiResponse(responseCode = "200", description = "내프로젝트 목록 조회 성공")
	@GetMapping(value = "my",produces = "application/json")
	public List<ProjectListResponseVO> myProjectList(
				@CurrentUser TokenParseResponseVO user){
		int empNo = user.getEmpNo();
		return projectService.selectMyProjectList(empNo);
	}
}
