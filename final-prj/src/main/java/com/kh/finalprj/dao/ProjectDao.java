package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.ProjectDto;
import com.kh.finalprj.vo.project.ProjectDetailResponseVO;
import com.kh.finalprj.vo.project.ProjectListResponseVO;

public interface ProjectDao {
	//등록
	int sequence();
	void add(ProjectDto projectDto);
	//내프로젝트 목록
	List<ProjectListResponseVO> selectMyProjectList(int empNo);
	//프로젝트 상세
	ProjectDetailResponseVO selectOne(int projectNo, int empNo);
	//프로젝트 수정
	boolean update(ProjectDto projectDto);
}
