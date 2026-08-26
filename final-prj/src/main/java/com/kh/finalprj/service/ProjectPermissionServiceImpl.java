package com.kh.finalprj.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dto.ProjectMemberDto;
import com.kh.finalprj.error.GetOutException;

@Service
public class ProjectPermissionServiceImpl implements ProjectPermissionService{
	@Autowired
	private ProjectMemberDao projectMemberDao;
	
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
	
}
