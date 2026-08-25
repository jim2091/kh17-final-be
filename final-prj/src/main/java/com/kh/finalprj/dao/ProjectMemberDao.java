package com.kh.finalprj.dao;

import com.kh.finalprj.dto.ProjectMemberDto;

public interface ProjectMemberDao {

	int sequence();
	void add(ProjectMemberDto projectMemberDto);
	
	Integer findProjectMemberNo(int projectNo, int empNo);
	ProjectMemberDto findMember(int projectNo, int empNo);
}
