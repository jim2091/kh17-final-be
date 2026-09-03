package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.ProjectExpectedResultDto;

public interface ProjectExpectedResultDao {

	//프로젝트 결과 상태 변경
	int updateStatus(int projectNo,int projectResultNo,String projectResultStatus);
	//번호발급
	int sequence();
	//다음 순서
	int nextOrder(int projectNo);
	//등록
	void add(ProjectExpectedResultDto projectExpectedResultDto);
	
	//목록
	List<ProjectExpectedResultDto> selectList(int projectNo);
	//상세
	ProjectExpectedResultDto find(int projectNo,int projectResultNo);
	//내용 수정
	boolean update(int projectNo,int projectResultNo,String projectResultContent);
	//삭제
	boolean delete(int projectNo,int projectResultNo);
	
}
