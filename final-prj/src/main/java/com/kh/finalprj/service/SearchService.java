package com.kh.finalprj.service;

import com.kh.finalprj.dto.ProjectHistoryResponseDto;
import com.kh.finalprj.dto.SearchDto;

public interface SearchService {

    SearchDto search(String keyword, String filter, int empNo);

	ProjectHistoryResponseDto searchProjectHistory(int empNo);
}