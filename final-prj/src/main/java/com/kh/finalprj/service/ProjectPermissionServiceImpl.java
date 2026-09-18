package com.kh.finalprj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.finalprj.dao.ProjectDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dto.ProjectDto;
import com.kh.finalprj.dto.ProjectMemberDto;
import com.kh.finalprj.error.GetOutException;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.error.WrongDataException;

@Service
public class ProjectPermissionServiceImpl implements ProjectPermissionService{
	@Autowired
	private ProjectMemberDao projectMemberDao;
	@Autowired
	private ProjectDao projectDao;
	
	@Override
	public ProjectMemberDto findMember(int projectNo, int empNo) {
		ProjectMemberDto projectMemberDto = projectMemberDao.findMember(projectNo, empNo);
		
		if (projectMemberDto == null)
			throw new GetOutException();
		
		return projectMemberDto;
	}
	
	@Override
	public int findProjectMemberNo(int projectNo, int empNo) {
		ProjectMemberDto projectMemberDto = findMember(projectNo, empNo);
		return projectMemberDto.getProjectMemberNo();
	}
	
	@Override
	public void checkMember(int projectNo, int empNo) {
		findMember(projectNo, empNo);
	}
	@Override
	public void checkOwner(int projectNo, int empNo) {
		ProjectMemberDto projectMemberDto = findMember(projectNo, empNo);
		if(!"owner".equals(projectMemberDto.getProjectMemberRole()))
			throw new GetOutException();
	}
	@Override
	public void checkOwnerOrManager(int projectNo, int empNo) {
		ProjectMemberDto projectMemberDto = findMember(projectNo, empNo);
		String role = projectMemberDto.getProjectMemberRole();
		if(!"owner".equals(role) && !"manager".equals(role))
			throw new GetOutException();
	}
	
	@Override
	public void checkActive(int projectNo) {
		ProjectDto projectDto = projectDao.selectProject(projectNo);
		
		if(projectDto == null)
			throw new TargetNotfoundException("존재하지 않는 프로젝트입니다");
		
		if(!"active".equals(projectDto.getProjectStatus()))
			throw new WrongDataException("종료된 프로젝트에서는 변경할 수 없습니다");
	}
	
	@Override
	public void checkReadPermission(int projectNo, int empNo) {

	    //프로젝트 조회
	    ProjectDto projectDto =
	            projectDao.selectProject(projectNo);

	    if(projectDto == null) {
	        throw new TargetNotfoundException(
	            "존재하지 않는 프로젝트입니다."
	        );
	    }

	    //현재 사용자가 프로젝트 멤버인지 확인
	    ProjectMemberDto projectMemberDto =
	            projectMemberDao.findMember(
	                projectNo,
	                empNo
	            );

	    //종료된 공개 프로젝트인지 확인
	    boolean publicClosed =
	            "public".equals(
	                projectDto.getProjectVisibility()
	            )
	            &&
	            "closed".equals(
	                projectDto.getProjectStatus()
	            );

	    //멤버도 아니고,
	    //종료된 공개 프로젝트도 아니라면 조회 불가
	    if(projectMemberDto == null && !publicClosed) {
	        throw new GetOutException(
	            "프로젝트 조회 권한이 없습니다."
	        );
	    }
	}
}
