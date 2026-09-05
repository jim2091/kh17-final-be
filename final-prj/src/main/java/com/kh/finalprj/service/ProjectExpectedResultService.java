package com.kh.finalprj.service;

import java.util.List;

import com.kh.finalprj.dto.ProjectExpectedResultDto;
import com.kh.finalprj.vo.project.ProjectExpectedResultRequestVO;
import com.kh.finalprj.vo.project.ProjectExpectedResultUpdateRequestVO;

public interface ProjectExpectedResultService {

	//프로젝트 결과 목록
	List<ProjectExpectedResultDto> resultList(int projectNo, int empNo);
	//프로젝트 결과 등록
	void resultAdd(
			int projectNo,
			ProjectExpectedResultRequestVO requestVO,
			int empNo
	);
	//프로젝트 결과 수정
	void resultUpdate(
			int projectNo,
			int projectResultNo,
			ProjectExpectedResultRequestVO requestVO,
			int empNo
	);
	//프로젝트 결과 삭제
	void resultDelete(int projectNo,int projectResultNo,int empNo);


}
