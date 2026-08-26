package com.kh.finalprj.service;

import com.kh.finalprj.dto.ProjectMemberDto;

public interface ProjectPermissionService {

	ProjectMemberDto findMember(int projectNo, int empNo);
	
	int findProjectMemberNo(int projectNo, int empNo);
	
	void checkMember(int projectNo, int empNo);
	
	void checkOwner(int projectNo, int empNo);
	
	void checkOwnerOrManager(int projectNo, int empNo);
	
}
