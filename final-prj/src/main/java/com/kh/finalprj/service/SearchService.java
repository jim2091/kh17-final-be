package com.kh.finalprj.service;

import java.util.List;

import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.dto.ProjectHistoryResponseDto;
import com.kh.finalprj.dto.SearchDto;

public interface SearchService {

	// 통합 검색
	SearchDto search(String keyword, String filter, int empNo);

	// 사용자 프로젝트 참여 이력
	ProjectHistoryResponseDto searchProjectHistory(int empNo);

	//사원 검색
	List<EmpDto> searchEmp(String keyword);
}