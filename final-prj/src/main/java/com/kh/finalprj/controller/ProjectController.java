package com.kh.finalprj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.service.ProjectService;
import com.kh.finalprj.vo.project.ProjectCreateRequestVO;

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
					@RequestParam int empNo) {

		return projectService.create(requestVO, empNo);
	}
}
