package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.kh.finalprj.dto.ProjectExpectedResultDto;
import com.kh.finalprj.service.ProjectExpectedResultService;
import com.kh.finalprj.service.ProjectService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.page.PageVO;
import com.kh.finalprj.vo.project.ProjectCloseRequestVO;
import com.kh.finalprj.vo.project.ProjectCreateRequestVO;
import com.kh.finalprj.vo.project.ProjectDetailResponseVO;
import com.kh.finalprj.vo.project.ProjectExpectedResultRequestVO;
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

		return projectService.create(requestVO,parseVO.getEmpNo());
	}
	
	//내 프로젝트 목록 매핑
	@ApiResponse(responseCode = "200", description = "내프로젝트 목록 조회 성공")
	@GetMapping(value = "/my",produces = "application/json")
	public List<ProjectListResponseVO> myProjectList(
				@CurrentUser TokenParseResponseVO parseVO
	){

		return projectService.selectMyProjectList(parseVO.getEmpNo());
	}
	
	//프로젝트 상세 조회 매핑
	@ApiResponse(responseCode = "200", description = "프로젝트 상세조회 성공")
	@GetMapping(value = "/{projectNo}",produces = "application/json")
	public ProjectDetailResponseVO detail(
			@PathVariable int projectNo,
			@CurrentUser TokenParseResponseVO parseVO
	) {

		return projectService.detail(projectNo, parseVO.getEmpNo());
	}
	
	//프로젝트 수정 매핑
	@ApiResponse(responseCode = "200",description = "프로젝트 수정 성공")
	@PutMapping(value = "/{projectNo}",produces = "application/json")
	public void update(
			@PathVariable int projectNo,
			@RequestBody ProjectUpdateRequestVO requestVO,
			@CurrentUser TokenParseResponseVO parseVO
	) {
		
		projectService.update(
				projectNo, 
				requestVO, 
				parseVO.getEmpNo()
		);
	}
	
	//공개 프로젝트 조회 매핑
	@ApiResponse(responseCode = "200",description = "공개 프로젝트 조회")
	@GetMapping(value = "/public",produces = "application/json")
	public PublicProjectListResponseVO publicProjectList(PageVO pageVO,
			@CurrentUser TokenParseResponseVO parseVO
	){
		
		return projectService.publicProjectList(pageVO,parseVO.getEmpNo());
	}
	
	//프로젝트 멤버 목록 매핑
	@ApiResponse(responseCode = "200",description = "프로젝트 멤버 목록 매핑")
	@GetMapping(value = "/{projectNo}/member",produces = "application/json")
	public List<ProjectMemberListResponseVO> memberList(
			@PathVariable int projectNo,@CurrentUser TokenParseResponseVO parseVO
	){
		return projectService.memberList(projectNo, parseVO.getEmpNo());
	}
	
	//역할 변경 매핑
	@ApiResponse(responseCode = "200",description = "멤버 역할 변경")
	@PatchMapping(value = "/{projectNo}/member/{projectMemberNo}/role", produces = "application/json")
	public void updateMemberRole(
		@PathVariable int projectNo,
		@PathVariable int projectMemberNo,
		@RequestBody ProjectMemberRoleUpdateRequestVO requestVO,
		@CurrentUser TokenParseResponseVO parseVO
	) {
		
		projectService.updateMemberRole(
			projectNo, 
			projectMemberNo, 
			requestVO.getProjectMemberRole(),
			parseVO.getEmpNo()
		);
	}
	
	//참여하기 매핑
	@ApiResponse(responseCode = "200",description = "공개 프로젝트 참여하기 성공")
	@PostMapping(value = "/{projectNo}/join",produces = "application/json")
	public void join (@PathVariable int projectNo, 
			@CurrentUser TokenParseResponseVO parseVO
	) {
		
		projectService.join(projectNo, parseVO.getEmpNo());
	}
	
	//owner 변경 매핑
	@ApiResponse(responseCode = "200", description = "owner변경 성공")
	@PatchMapping(value = "/{projectNo}/owner/{projectMemberNo}",produces = "application/json")
	public void changeOwner(
			@PathVariable int projectNo,
			@PathVariable int projectMemberNo,
			@CurrentUser TokenParseResponseVO parseVO
	) {
		
		projectService.changeOwner(
				projectNo, 
				projectMemberNo, 
				parseVO.getEmpNo()
		);
	}
	
	//프로젝트 삭제 매핑
	@ApiResponse(responseCode = "200",description = "프로젝트 삭제 성공")
	@DeleteMapping(value = "/{projectNo}",produces = "application/json")
	public void delete(
			@PathVariable int projectNo,
			@CurrentUser TokenParseResponseVO parseVO
	) {
		
		projectService.delete(projectNo,parseVO.getEmpNo());
		
	}
	
	//프로젝트 종료 매핑
	@ApiResponse(responseCode = "200",description = "프로젝트 종료")
	@PatchMapping(value = "/{projectNo}/close", produces ="application/json" )
	public void close(
			@PathVariable int projectNo,
			@RequestBody ProjectCloseRequestVO requestVO,
			@CurrentUser TokenParseResponseVO parseVO
			
	) {
		
		projectService.close(
				projectNo, 
				requestVO, 
				parseVO.getEmpNo()
		);
	}

	//아카이브 목록 매핑
	@ApiResponse(responseCode = "200",description = "아카이브 목록")
	@GetMapping(value="/archive",produces = "application/json")
	public List<ProjectListResponseVO> archiveProjectList(
			@CurrentUser TokenParseResponseVO parseVO
	){
		
		return projectService.archiveProjectList(parseVO.getEmpNo());
		
	}
	
	//프로젝트 재활성화
	@ApiResponse(responseCode = "200",description = "프로젝트 재활성화")
	@PatchMapping(value = "/{projectNo}/activate",produces = "application/json")
	public void activate(
			@PathVariable int projectNo,
			@CurrentUser TokenParseResponseVO parseVO
	) {
		
		projectService.activate(projectNo, parseVO.getEmpNo());
	}
	
	//프로젝트 탈퇴
	@ApiResponse(responseCode = "200",description = "프로젝트 탈퇴 성공")
	@DeleteMapping(value = "/{projectNo}/member/leave",produces = "application/json")
	public void leave(
			@PathVariable int projectNo,
			@RequestParam (required=false) Integer newOwnerMemberNo,
			@CurrentUser TokenParseResponseVO parseVO
	) {
		
		projectService.leave(
				projectNo, 
				parseVO.getEmpNo(), 
				newOwnerMemberNo
		);
	}
	
	//프로젝트 멤버 강제퇴장
	@ApiResponse(responseCode = "200",description = "프로젝트 강제 퇴장")
	@DeleteMapping(value = "/{projectNo}/member/{projectMemberNo}", produces = "application/json")
	public void kickMember (
			@PathVariable int projectNo,
			@PathVariable int projectMemberNo,
			@CurrentUser TokenParseResponseVO parseVO
	) {
		
		projectService.kickMember(
				projectNo, 
				projectMemberNo, 
				parseVO.getEmpNo()
		);
	}
}
