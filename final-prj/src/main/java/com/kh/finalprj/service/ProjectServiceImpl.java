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
import com.kh.finalprj.vo.jwt.TokenCreateRequestVO;
import com.kh.finalprj.vo.project.ProjectCreateRequestVO;
import com.kh.finalprj.vo.project.ProjectListResponseVO;

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
		//프로젝트 번호 발급
		int projectNo = projectDao.sequence();
		
		//프로젝트 생성
		ProjectDto projectDto = ProjectDto.builder()
				.projectNo(projectNo)
				.projectName(requestVO.getProjectName())
				.projectPurpose(requestVO.getProjectPurpose())
				.projectVisibility(requestVO.getProjectVisibility())
				.projectStart(requestVO.getProjectStart())
				.projectDeadline(requestVO.getProjectDeadline())
			.build();
		
		//DB에 추가
		projectDao.add(projectDto);
	
		//owner로 등록
		int projectMemberNo = projectMemberDao.sequence();
		
		ProjectMemberDto projectMemberDto = ProjectMemberDto.builder()
				.projectMemberNo(projectMemberNo)
				.projectNo(projectNo)
				.empNo(empNo)
				.projectMemberRole("owner")
			.build();
		
		projectMemberDao.add(projectMemberDto);
		
		//#general 채널 자동 생성
		int channelNo = channelDao.sequence();
		
		ChannelDto channelDto = ChannelDto.builder()
					.chatChannelNo(channelNo)
					.projectNo(projectNo)
					.chatChannelCreator(projectMemberNo)
					.chatChannelName("#general")
				.build();
		
		channelDao.create(channelDto);
		
		return projectNo;
	}

	//내 프로젝트 조회
	@Override
	public List<ProjectListResponseVO> selectMyProjectList(int empNo) {
		return projectDao.selectMyProjectList(empNo);
	}

	
	
	
}
