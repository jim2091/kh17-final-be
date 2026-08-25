package com.kh.finalprj.service;

import java.util.List;

import com.kh.finalprj.vo.project.ProjectCreateRequestVO;
import com.kh.finalprj.vo.project.ProjectListResponseVO;

public interface ProjectService{
	
	//프로젝트 생성
	int create(ProjectCreateRequestVO requestVO, int empNo);
	//내 프로젝트 목록
	List<ProjectListResponseVO> selectMyProjectList(int empNo);

}
