package com.kh.finalprj.service;

import java.util.List;

import com.kh.finalprj.dto.ProjectHistoryDto;
import com.kh.finalprj.dto.SearchDto;

public interface SearchService {

    SearchDto search(String keyword, String filter, int empNo);

    List<ProjectHistoryDto> searchProjectHistory(int empNo);
}