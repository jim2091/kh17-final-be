package com.kh.finalprj.service;

import java.util.List;

import com.kh.finalprj.vo.page.PageVO;
import com.kh.finalprj.vo.project.ProjectCreateRequestVO;
import com.kh.finalprj.vo.project.ProjectDetailResponseVO;
import com.kh.finalprj.vo.project.ProjectListResponseVO;
import com.kh.finalprj.vo.project.ProjectMemberListResponseVO;
import com.kh.finalprj.vo.project.ProjectUpdateRequestVO;
import com.kh.finalprj.vo.project.PublicProjectListResponseVO;

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
	PublicProjectListResponseVO publicProjectList(PageVO pageVO,int empNo);
	//프로젝트 멤버 목록
	List<ProjectMemberListResponseVO> memberList(int projectNo, int empNo);
	//멤버 권한 수정
	void updateMemberRole(int projectNo,int projectMemberNo,String projectMemberRole, int empNo);
	
}
