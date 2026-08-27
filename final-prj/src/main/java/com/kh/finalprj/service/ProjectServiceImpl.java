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
import com.kh.finalprj.vo.jwt.TokenCreateRequestVO;
import com.kh.finalprj.vo.project.ProjectCreateRequestVO;
import com.kh.finalprj.vo.project.ProjectDetailResponseVO;
import com.kh.finalprj.vo.project.ProjectListResponseVO;
import com.kh.finalprj.vo.project.ProjectUpdateRequestVO;

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
	public List<ProjectListResponseVO> publicProjectList(int empNo, String keyword, int page) {
		
//		int pageSize = 9;
//		
//		//전체 개수
//		int totalCount = projectDao.countPublicProject(keyword);
//		
//		//전체 페이지
//		int totalPage = (totalCount + pageSize-1)/pageSize;
//		
//		//현재 페이지 시작/끝 위치
//		int startRow =
		return null;
	}

	
	
	
}
