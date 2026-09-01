package com.kh.finalprj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.ChannelDao;
import com.kh.finalprj.dao.ProjectDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dto.ChannelDto;
import com.kh.finalprj.dto.ProjectDto;
import com.kh.finalprj.dto.ProjectMemberDto;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.error.WhoAreYouException;
import com.kh.finalprj.error.WrongDataException;
import com.kh.finalprj.vo.page.PageVO;
import com.kh.finalprj.vo.project.ProjectCreateRequestVO;
import com.kh.finalprj.vo.project.ProjectDetailResponseVO;
import com.kh.finalprj.vo.project.ProjectListResponseVO;
import com.kh.finalprj.vo.project.ProjectMemberListResponseVO;
import com.kh.finalprj.vo.project.ProjectUpdateRequestVO;
import com.kh.finalprj.vo.project.PublicProjectListResponseVO;

//프로젝트 관련 작업을 처리하기 위한 서비스
@Service
public class ProjectServiceImpl implements ProjectService{

	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private ProjectMemberDao projectMemberDao;
	@Autowired
	private ChannelDao channelDao;

	//프로젝트 생성
	@Transactional
	@Override
	public int create(ProjectCreateRequestVO requestVO, int empNo) {
		//1.프로젝트 번호 발급
		int projectNo = projectDao.sequence();
		
		//2.프로젝트 생성
		ProjectDto projectDto = ProjectDto.builder()
				.projectNo(projectNo)
				.projectName(requestVO.getProjectName())
				.projectPurpose(requestVO.getProjectPurpose())
				.projectVisibility(requestVO.getProjectVisibility())
				.projectStart(requestVO.getProjectStart())
				.projectDeadline(requestVO.getProjectDeadline())
			.build();
		
		//3.DB에 추가
		projectDao.add(projectDto);
	
		//4.owner로 등록
		int projectMemberNo = projectMemberDao.sequence();
		
		//5.Dto 생성
		ProjectMemberDto projectMemberDto = ProjectMemberDto.builder()
				.projectMemberNo(projectMemberNo)
				.projectNo(projectNo)
				.empNo(empNo)
				.projectMemberRole("owner")
			.build();
		
		//6.등록
		projectMemberDao.add(projectMemberDto);
		
		//7.#general 채널 자동 생성
		int channelNo = channelDao.sequence();
		
		ChannelDto channelDto = ChannelDto.builder()
					.chatChannelNo(channelNo)
					.projectNo(projectNo)
					.chatChannelCreator(projectMemberNo)
					.chatChannelName("#general")
				.build();
		
		//8.채널 DB에 추가
		channelDao.create(channelDto);
		
		return projectNo;
	}

	//내 프로젝트 조회
	@Override
	public List<ProjectListResponseVO> selectMyProjectList(int empNo) {
		return projectDao.selectMyProjectList(empNo);
	}

	//프로젝트 상세 조회
	@Override
	public ProjectDetailResponseVO detail(int projectNo, int empNo) {
		ProjectDetailResponseVO project = projectDao.selectOne(projectNo, empNo);
		//프로젝트가 없는경우
		if(project == null) throw new TargetNotfoundException();
		
		return project;
	}

	//프로젝트 수정
	@Transactional
	@Override
	public void update(int projectNo, ProjectUpdateRequestVO requestVO, int empNo) {
		//1.프로젝트 권한 확인
		String role = projectMemberDao.selectRole(projectNo, empNo);
		
		//2.프로젝트 참여자가 아닌경우
		if(role == null) {
			throw new WhoAreYouException();
		}
		//3.owner가 아닌경우
		if(!role.equals("owner")) {
			throw new WhoAreYouException();
		}
		
		//4.날짜 검사
		if(requestVO.getProjectStart() != null &&
			requestVO.getProjectDeadline() != null &&
			requestVO.getProjectStart().after(requestVO.getProjectDeadline())
		){
			throw new TargetNotfoundException("프로젝트 마감일은 시작일 이후여야 합니다.");
			
		}
		
		//5.projectDto 생성
		ProjectDto projectDto = ProjectDto.builder()
				.projectNo(projectNo)
				.projectName(requestVO.getProjectName())
				.projectPurpose(requestVO.getProjectPurpose())
				.projectVisibility(requestVO.getProjectVisibility())
				.projectStart(requestVO.getProjectStart())
				.projectDeadline(requestVO.getProjectDeadline())
				.build();
		
		//6.수정
		projectDao.update(projectDto);
	}

	

	//공개 프로젝트 목록
	@Override
	public PublicProjectListResponseVO publicProjectList(PageVO pageVO,int empNo) {
		//1.공개 프로젝트 전체 개수
		int count = projectDao.countPublicProject(pageVO);
		
		//2.pageVO에 전체 개수 설정
		pageVO.setCount(count);
		
		//3.현재 페이지 프로젝트 목록 조회
		List<ProjectListResponseVO> projectList = 
				projectDao.selectPublicProjectList(pageVO, empNo);
		
		//4.페이지 위치,목록반환
		
		return PublicProjectListResponseVO.builder()
					.pageVO(pageVO)
					.projectList(projectList)
				.build();
	}

	//프로젝트 멤버 목록
	@Override
	public List<ProjectMemberListResponseVO> memberList(int projectNo, int empNo) {
		//프로젝트 참여자인지 확인
		String role = projectMemberDao.selectRole(projectNo, empNo);
		
		if(role == null) {
			throw new WhoAreYouException("프로젝트 권한이 없습니다.");
		}
		return projectMemberDao.selectProjectMemberList(projectNo);
	}

	//멤버 권한 수정
	@Override
	public void updateMemberRole(int projectNo, int projectMemberNo, String projectMemberRole, int empNo) {
		//1. 권한 확인
		String loginUserRole = projectMemberDao.selectRole(projectNo, empNo);
		
		if(!loginUserRole.equals("owner")) {
			throw new WhoAreYouException("프로젝트 멤버 역할 변경 권한이 없습니다.");	
		}
		//2.변경 가능한 역할인지 확인
		if(!projectMemberRole.equals("manager") && 
			!projectMemberRole.equals("member")) {
			throw new WhoAreYouException("변경할 수 없는 프로젝트 권한입니다.");
		}
		
		int result = projectMemberDao.updateRole(
				projectNo,
				projectMemberNo,
				projectMemberRole
		);
		
		if(result == 0) {
			throw new TargetNotfoundException("변경할 프로젝트 멤버를 찾을 수 없습니다.");
		}
		
	}
	
	//프로젝트 참가
	@Override
	public void join(int projectNo, int empNo) {
		//1.프로젝트 존재 여부 확인
		ProjectDto project = projectDao.selectProject(projectNo);
		
		if(project == null) {
			throw new TargetNotfoundException("존재하지 않는 프로젝트입니다.");	
		}
		
		//2.공개 프로젝트인지 확인
		if(!project.getProjectVisibility().equals("public")){
			throw new TargetNotfoundException("공개 프로젝트가 아닙니다.");
		}
		
		//3.진행중인 프로젝트인지 확인
		if(!project.getProjectStatus().equals("active")) {
			throw new TargetNotfoundException("종료된 프로젝트에는 참가할 수 없습니다.");
		}
		
		//4.이미 참여중인지 확인
		String role = projectMemberDao.selectRole(projectNo, empNo);
		if(role != null) {
			throw new WrongDataException("이미 참여중인 프로젝트입니다.");
		}
		
		//5.프로젝트 멤버 번호 발급
		int projectMemberNo = projectMemberDao.sequence();
		
		//6.멤버 생성
		ProjectMemberDto projectMemberDto = ProjectMemberDto.builder()
					.projectMemberNo(projectMemberNo)
					.projectNo(projectNo)
					.empNo(empNo)
					.projectMemberRole("member")
				.build();
		
		//7.프로젝트 참가
		projectMemberDao.add(projectMemberDto);
		
	}

	//owner 변경
	@Transactional
	@Override
	public void changeOwner(int projectNo, int projectMemberNo, int empNo) {
		//1.현재 로그인 사용자의 프로젝트 멤버 정보 조회
		ProjectMemberDto loginMember = projectMemberDao.findMember(projectNo, empNo);
		
		//참여자가 아닌 경우
		if(loginMember == null) {
			throw new WhoAreYouException("프로젝트 참여자가 아닙니다.");
		}
		
		//2.owner인지 확인
		if(!loginMember.getProjectMemberRole().equals("owner")) {
			throw new WhoAreYouException("owner 변경 권한이 없습니다");
		}
		
		//3.새 owner 대상 조회
		ProjectMemberDto targetMember = projectMemberDao.findMember(projectMemberNo);
		
		if(targetMember == null) {
			throw new TargetNotfoundException("변경할 프로젝트 멤버가 존재하지 않습니다.");
		}
		
		//4.같은 프로젝트 멤버인지 확인
		if(targetMember.getProjectNo() != projectNo) {
			throw new WhoAreYouException("해당 프로젝트의 멤버가 아닙니다.");
		}
		
		//5.자기 자신으로 변경 방지
		if(loginMember.getProjectMemberNo() == targetMember.getProjectMemberNo()) {
			throw new WrongDataException("이미 프로젝트 owner입니다.");
		}
		
		//6. 기존owner->manager
		projectMemberDao.updateRole(
				projectNo, 
				loginMember.getProjectMemberNo(), 
				"manager"
			);
		
		//7. 대상멤버 ->owner
		projectMemberDao.updateRole(
				projectNo, 
				targetMember.getProjectMemberNo(), 
				"owner"
			);
	}

	


	
	
	
}
