package com.kh.finalprj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.ChannelDao;
import com.kh.finalprj.dao.ProjectCloseDao;
import com.kh.finalprj.dao.ProjectDao;
import com.kh.finalprj.dao.ProjectExpectedResultDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dao.TaskDao;
import com.kh.finalprj.dto.ChannelDto;
import com.kh.finalprj.dto.ProjectCloseDto;
import com.kh.finalprj.dto.ProjectDto;
import com.kh.finalprj.dto.ProjectExpectedResultDto;
import com.kh.finalprj.dto.ProjectMemberDto;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.error.WhoAreYouException;
import com.kh.finalprj.error.WrongDataException;
import com.kh.finalprj.vo.page.PageVO;
import com.kh.finalprj.vo.project.ProjectCloseRequestVO;
import com.kh.finalprj.vo.project.ProjectCreateRequestVO;
import com.kh.finalprj.vo.project.ProjectDetailResponseVO;
import com.kh.finalprj.vo.project.ProjectListResponseVO;
import com.kh.finalprj.vo.project.ProjectMemberListResponseVO;
import com.kh.finalprj.vo.project.ProjectResultCloseRequestVO;
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
	@Autowired
	private TaskDao taskDao;
	@Autowired
	private ProjectCloseDao projectCloseDao;
	@Autowired
	private ProjectExpectedResultDao projectExpectedResultDao;

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
		if(!"owner".equals(role)) {
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
		
		if(!"owner".equals(loginUserRole)) {
			throw new WhoAreYouException("프로젝트 멤버 역할 변경 권한이 없습니다.");	
		}
		//2.변경 가능한 역할인지 확인
		if(!"manager".equals(projectMemberRole) && 
			!"member".equals(projectMemberRole)) {
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
		if(!"owner".equals(loginMember.getProjectMemberRole())) {
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

	//프로젝트 삭제
	@Transactional
	@Override
	public void delete(int projectNo, int empNo) {
		//1.프로젝트 존재 확인
		ProjectDto project = projectDao.selectProject(projectNo);
		
		if(project == null) {
			throw new TargetNotfoundException("존재하지 않는 프로젝트입니다.");
		}
		
		//2.현재 사용자의 프로젝트 권한 확인
		String role = projectMemberDao.selectRole(projectNo, empNo);
		
		//프로젝트 참여자가 아닌 경우
		if(role == null) {
			throw new WhoAreYouException("프로젝트 참여자가 아닙니다.");
		}
		
		//3.owner만 삭제 가능
		if(!"owner".equals(role)) {
			throw new WhoAreYouException("프로젝트 삭제 권한이 없습니다.");
		}
		
		//4.프로젝트의 업무 개수 확인
		int taskCount = taskDao.countByProjectNo(projectNo);
		
		//업무가 있으면 삭제 불가
		if(taskCount > 0 ) {
			throw new WrongDataException("업무가 존재하는 프로젝트는 삭제할 수 없습니다.");
		}
		
		//5.프로젝트 삭제
		boolean result = projectDao.delete(projectNo);
		
		if(result == false) {
			throw new TargetNotfoundException("프로젝트 삭제에 실패했습니다.");
		}
	}
	
	//프로젝트 종료
	@Transactional
	@Override
	public void close(int projectNo, ProjectCloseRequestVO requestVO, int empNo) {
		
		//1.프로젝트 확인
		ProjectDto project = projectDao.selectProject(projectNo);
		
		if(project == null) {
			throw new TargetNotfoundException("존재하지 않는 프로젝트입니다.");
		}
		
		//2.프로젝트 참여 권한 확인
		String role = projectMemberDao.selectRole(projectNo, empNo);

		//참여자가 아닌경우
		if(role == null) {
			throw new WhoAreYouException("프로젝트 참여자가 아닙니다.");
		}
		
		//3.owner인지 확인
		if(!"owner".equals(role)) {
			throw new WhoAreYouException("프로젝트 권한이 없습니다.");
			
		}

		//4.이미 종료된 프로젝트인지 확인
		if(project.getProjectStatus().equals("closed")) {
			throw new WrongDataException("이미 종료된 프로젝트 입니다.");
		}
		
		//5.종료 요약 검사
		if(requestVO.getCloseSummary() == null ||
			requestVO.getCloseSummary().isBlank()) {
			throw new WrongDataException("프로젝트 요약을 작성해주세요.");
		}

		//6.예상 결과 평가
		if(requestVO.getResultList() != null) {
			for(ProjectResultCloseRequestVO result
				: requestVO.getResultList()) {
				//상태 검사
				if(!result.getProjectResultStatus().equals("achieved")
					&&
				!result.getProjectResultStatus().equals("unachieved")
				) {
					throw new WrongDataException("올바르지 않은 예상 결과 상태입니다.");
				}
				
				//예상 결과 상태 변경
				int updateResult = projectExpectedResultDao.updateStatus(
						projectNo,
						result.getProjectResultNo(),
						result.getProjectResultStatus()
				);
				
				
				//해당 프로젝트의 예상 결과가 아닌 경우
				if(updateResult == 0) {
					throw new TargetNotfoundException("예상 결과 정보를 찾을 수 없습니다.");
				}
			}
		}
		//7.종료DTO생성
		ProjectCloseDto projectCloseDto = ProjectCloseDto.builder()
				.projectNo(projectNo)
				.closeSummary(requestVO.getCloseSummary())
				.closeGood(requestVO.getCloseGood())
				.closeBad(requestVO.getCloseBad())
				.closeImprovement(requestVO.getCloseImprovement())
			.build();

		//8.프로젝트 종료 정보 등록
		projectCloseDao.add(projectCloseDto);

		//9.프로젝트 상태 closed로 변경
		boolean closeResult = projectDao.close(projectNo);
		
		if(closeResult == false) {
			throw new WrongDataException("프로젝트를 종료할 수 없습니다.");
		}
	}


	


	
	
	
}
