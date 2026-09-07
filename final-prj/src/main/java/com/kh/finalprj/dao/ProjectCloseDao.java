package com.kh.finalprj.dao;

import com.kh.finalprj.dto.ProjectCloseDto;

public interface ProjectCloseDao{
	//프로그램 종료 등록
	void add(ProjectCloseDto projectCloseDto);
	//프로그램 종료 정보 삭제
	void delete(int projectNo);
}
