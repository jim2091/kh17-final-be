package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.service.ProjectService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.page.PageVO;
import com.kh.finalprj.vo.project.ProjectCreateRequestVO;
import com.kh.finalprj.vo.project.ProjectDetailResponseVO;
import com.kh.finalprj.vo.project.ProjectListResponseVO;
import com.kh.finalprj.vo.project.ProjectMemberListResponseVO;
import com.kh.finalprj.vo.project.ProjectMemberRoleUpdateRequestVO;
import com.kh.finalprj.vo.project.ProjectUpdateRequestVO;
import com.kh.finalprj.vo.project.PublicProjectListResponseVO;

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
	public int create(
			@RequestBody ProjectCreateRequestVO requestVO,
			@CurrentUser TokenParseResponseVO parseVO
	) {
		int empNo = parseVO.getEmpNo();
		return projectService.create(requestVO,empNo);
	}
	
	//내 프로젝트 목록 매핑
	@ApiResponse(responseCode = "200", description = "내프로젝트 목록 조회 성공")
	@GetMapping(value = "/my",produces = "application/json")
	public List<ProjectListResponseVO> myProjectList(
				@CurrentUser TokenParseResponseVO parseVO
	){
		int empNo = parseVO.getEmpNo();
		return projectService.selectMyProjectList(empNo);
	}
	
	//프로젝트 상세 조회 매핑
	@ApiResponse(responseCode = "200", description = "프로젝트 상세조회 성공")
	@GetMapping(value = "/{projectNo}",produces = "application/json")
	public ProjectDetailResponseVO detail(
			@PathVariable int projectNo,
			@CurrentUser TokenParseResponseVO parseVO
	) {
		int empNo = parseVO.getEmpNo();
		return projectService.detail(projectNo, empNo);
	}
	
	//프로젝트 수정 매핑
	@ApiResponse(responseCode = "200",description = "프로젝트 수정 성공")
	@PutMapping(value = "/{projectNo}",produces = "application/json")
	public void update(
			@PathVariable int projectNo,
			@RequestBody ProjectUpdateRequestVO requestVO,
			@CurrentUser TokenParseResponseVO parseVO
	) {
		int empNo = parseVO.getEmpNo();
		projectService.update(projectNo, requestVO, empNo);
	}
	
	//공개 프로젝트 조회 매핑
	@ApiResponse(responseCode = "200",description = "공개 프로젝트 조회")
	@GetMapping(value = "/public",produces = "application/json")
	public PublicProjectListResponseVO publicProjectList(PageVO pageVO,
			@CurrentUser TokenParseResponseVO parseVO
	){
		int empNo = parseVO.getEmpNo();
		return projectService.publicProjectList(pageVO,empNo);
	}
	
	//프로젝트 멤버 목록 매핑
	@ApiResponse(responseCode = "200",description = "프로젝트 멤버 목록 매핑")
	@GetMapping("/{projectNo}/member")
	public List<ProjectMemberListResponseVO> memberList(
			@PathVariable int projectNo,@CurrentUser TokenParseResponseVO parseVO
	){
		int empNo = parseVO.getEmpNo();
		return projectService.memberList(projectNo, empNo);
	}
	
	//역할 변경
	@ApiResponse(responseCode = "200",description = "멤버 역할 변경")
	@PatchMapping("/{projectNo}/member/{projectMemberNo}/role")
	public void updateMemberRole(
		@PathVariable int projectNo,
		@PathVariable int projectMemberNo,
		@PathVariable String projectVisibility,
		@RequestBody ProjectMemberRoleUpdateRequestVO requestVO,
		@CurrentUser TokenParseResponseVO parseVO
	) {
		int empNo = parseVO.getEmpNo();
		
		projectService.updateMemberRole(
			projectNo, 
			projectMemberNo, 
			requestVO.getProjectMemberRole(), 
			empNo, projectVisibility
		);
	}
}
