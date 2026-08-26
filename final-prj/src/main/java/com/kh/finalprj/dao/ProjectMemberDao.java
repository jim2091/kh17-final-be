package com.kh.finalprj.dao;

import com.kh.finalprj.dto.ProjectMemberDto;

public interface ProjectMemberDao {

	//프로젝트에 멤버 추가
	int sequence();
	void add(ProjectMemberDto projectMemberDto);
	//멤버 역할 조회
	String selectRole(int projectNo, int empNo);
	
	//프로젝트 멤버 번호 조회
	Integer findProjectMemberNo(int projectNo, int empNo);
	//프로젝트 멤버 상세 조회
	ProjectMemberDto findMember(int projectNo, int empNo);
	
	//필요해서 추가함 - 정빈
	ProjectMemberDto findMember(int projectMemberNo);
}
