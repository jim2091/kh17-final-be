package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.ProjectDto;
import com.kh.finalprj.vo.page.PageVO;
import com.kh.finalprj.vo.project.ProjectDetailResponseVO;
import com.kh.finalprj.vo.project.ProjectListResponseVO;

public interface ProjectDao {
	//등록
	int sequence();
	void add(ProjectDto projectDto);
	//내 프로젝트 목록
	List<ProjectListResponseVO> selectMyProjectList(int empNo);
	//프로젝트 상세
	ProjectDetailResponseVO selectOne(int projectNo, int empNo);
	//프로젝트 수정
	boolean update(ProjectDto projectDto);
	
	//공개 프로젝트 개수
	int countPublicProject(PageVO pageVO);
	//공개 프로젝트 목록
	List<ProjectListResponseVO> selectPublicProjectList(PageVO pageVO, int empNo);
	//프로젝트 자체 조회(참여하기에 사용)
	ProjectDto selectProject(int projectNo);
	
}
