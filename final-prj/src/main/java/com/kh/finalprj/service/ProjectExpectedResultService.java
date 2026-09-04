package com.kh.finalprj.service;

import java.util.List;

import com.kh.finalprj.dto.ProjectExpectedResultDto;
import com.kh.finalprj.vo.project.ProjectExpectedResultRequestVO;

public interface ProjectExpectedResultService {
	
	//기대결과 목록
	List<ProjectExpectedResultDto>resultList(int projectNo,int empNo);
	//기대결과 생성
	void addResult(int projectNo,ProjectExpectedResultRequestVO requestVO,int empNo);
	//기대결과 수정
	void updateResult(
			ProjectExpectedResultRequestVO requestVO,int empNo,
			int projectNo, int projectResultNo
	);
	//기대결과 삭제
	void deleteResult(int projectNo,int projectResultNo,int empNo);
	
}
