package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.ProjectExpectedResultDto;

public interface ProjectExpectedResultDao {

	//프로젝트 기대결과 생성
	int sequence();
	void add(ProjectExpectedResultDto dto);
	//프로젝트 결과 목록
	List<ProjectExpectedResultDto> selectList(int projectNo);
	//기대 결과 내용 수정
//	boolean update(int projectNo,int projectResultNo,String projectResultContent);
	//기대 결과 삭제
	boolean delete(int projectNo,int projectResultNo);
	//프로젝트 결과 상태 변경
	int updateStatus(int projectNo,int projectResultNo,String projectResultStatus);

	
}
