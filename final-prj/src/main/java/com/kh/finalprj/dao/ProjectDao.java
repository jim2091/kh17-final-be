package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.ProjectDto;
import com.kh.finalprj.vo.project.ProjectListResponseVO;

public interface ProjectDao {
	//등록
	int sequence();
	void add(ProjectDto projectDto);
	//내프로젝트 목록
	List<ProjectListResponseVO> selectMyProjectList(int empNo);
}
