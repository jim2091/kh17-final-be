package com.kh.finalprj.dao;

import com.kh.finalprj.dto.ProjectDto;

public interface ProjectDao {

	int sequence();
	void add(ProjectDto projectDto);
}
