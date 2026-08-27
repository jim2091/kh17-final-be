package com.kh.finalprj.service;

import java.util.List;

import com.kh.finalprj.vo.project.ProjectCreateRequestVO;
import com.kh.finalprj.vo.project.ProjectDetailResponseVO;
import com.kh.finalprj.vo.project.ProjectListResponseVO;
import com.kh.finalprj.vo.project.ProjectUpdateRequestVO;

public interface ProjectService{
	
	//프로젝트 생성
	int create(ProjectCreateRequestVO requestVO, int empNo);
	//내 프로젝트 목록
	List<ProjectListResponseVO> selectMyProjectList(int empNo);
	//프로젝트 상세조회
	ProjectDetailResponseVO detail(int projectNo, int empNo);
	//프로젝트 수정
	void update(int projectNo, ProjectUpdateRequestVO requestVO, int empNo);
	//공개 프로젝트 목록
	List<ProjectListResponseVO> publicProjectList(int empNo,String keyword,int page);
}
