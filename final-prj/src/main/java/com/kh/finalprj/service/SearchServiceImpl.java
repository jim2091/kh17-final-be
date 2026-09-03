package com.kh.finalprj.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.SearchDao;
import com.kh.finalprj.dto.SearchDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {

    private final SearchDao searchDao;


    @Override
    public SearchDto search(
            String keyword,
            String filter,
            int empNo
    ) {

        // ========================================
        // 검색어 정리
        // ========================================

        if (keyword == null) {
            keyword = "";
        }

        keyword = keyword.trim();


        // ========================================
        // 필터 정리
        // ========================================

        if (filter == null || filter.trim().isEmpty()) {
            filter = "all";
        }

        filter = filter.trim().toLowerCase();


        // ========================================
        // 결과 객체
        // ========================================

        SearchDto result = new SearchDto();

        result.setKeyword(keyword);
        result.setFilter(filter);

        result.setUsers(new ArrayList<>());
        result.setProjects(new ArrayList<>());
        result.setTasks(new ArrayList<>());
        result.setRecords(new ArrayList<>());
        result.setNotes(new ArrayList<>());
        result.setFiles(new ArrayList<>());


        // ========================================
        // 검색어 없음
        // ========================================

        if (keyword.isEmpty()) {
            return result;
        }


        // ========================================
        // 전체 검색
        // ========================================

        if ("all".equals(filter)) {

            result.setUsers(
                    searchDao.searchMembers(keyword)
            );

            result.setProjects(
                    searchDao.searchProjects(keyword, empNo)
            );

            result.setTasks(
                    searchDao.searchTasks(keyword)
            );

            result.setFiles(
                    searchDao.searchFiles(keyword)
            );

            return result;
        }


        // ========================================
        // 다중 필터 처리
        // ========================================

        Set<String> filters = new HashSet<>(
                Arrays.asList(filter.split(","))
        );


        // ========================================
        // 사용자
        // ========================================

        if (
                filters.contains("user") ||
                filters.contains("users")
        ) {

            result.setUsers(
                    searchDao.searchMembers(keyword)
            );
        }


        // ========================================
        // 프로젝트
        // ========================================

        if (
                filters.contains("project") ||
                filters.contains("projects")
        ) {

            result.setProjects(
                    searchDao.searchProjects(keyword, empNo)
            );
        }


        // ========================================
        // 업무
        // ========================================

        if (
                filters.contains("task") ||
                filters.contains("tasks")
        ) {

            result.setTasks(
                    searchDao.searchTasks(keyword)
            );
        }


        // ========================================
        // 기록
        // ========================================

        if (
                filters.contains("record") ||
                filters.contains("records")
        ) {

            // 아직 구현하지 않음
        }


        // ========================================
        // 노트
        // ========================================

        if (
                filters.contains("note") ||
                filters.contains("notes")
        ) {

            // 아직 구현하지 않음
        }


        // ========================================
        // 파일
        // ========================================

        if (
                filters.contains("file") ||
                filters.contains("files")
        ) {

            result.setFiles(
                    searchDao.searchFiles(keyword)
            );
        }


        return result;
    }

}