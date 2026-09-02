package com.kh.finalprj.dao;

public interface ProjectExpectedResultDao {

	//프로젝트 결과 상태 변경
	int updateStatus(int projectNo,int projectResultNo,String projectResultStatus);
}
