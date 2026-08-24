package com.kh.finalprj.dao;

import com.kh.finalprj.dto.ProjectMemberDto;

public interface ProjectMemberDao {

	int sequence();
	void add(ProjectMemberDto projectMemberDto);
}
