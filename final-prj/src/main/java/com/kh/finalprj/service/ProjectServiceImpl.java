package com.kh.finalprj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.ProjectDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dto.ProjectDto;
import com.kh.finalprj.dto.ProjectMemberDto;
import com.kh.finalprj.vo.ProjectCreateRequestVO;

//프로젝트 관련 작업을 처리하기 위한 서비스
@Service
public class ProjectServiceImpl implements ProjectService{

	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private ProjectMemberDao projectMemberDao;

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
	
		//owner로 등록
		int projectMemberNo = projectMemberDao.sequence();
		
		ProjectMemberDto projectMemberDto = ProjectMemberDto.builder()
				.projectMemberNo(projectMemberNo)
				.projectNo(projectNo)
				.empNo(empNo)
				.projectMemberRole("owner")
			.build();
		
		projectMemberDao.add(projectMemberDto);
		
		return projectNo;
	}
	
	
	
}
