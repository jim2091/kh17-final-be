package com.kh.finalprj.dto;

import java.util.List;

import lombok.Data;

@Data
public class SearchDto {

    // ========================================
    // 검색 조건
    // ========================================

    // 검색어
    private String keyword;

    // 검색 필터
    // all
    // user
    // project
    // task
    // record
    // note
    // file
    private String filter;


    // ========================================
    // 검색 결과
    // ========================================

    // 사용자
    private List<EmpDto> users;

    // 프로젝트
    private List<ProjectDto> projects;

    // 업무
    private List<TaskDto> tasks;

    // Records
    // 아직 테이블이 없으므로 현재는 빈 배열
    private List<Object> records;

    // 노트
    // 아직 테이블이 없으므로 현재는 빈 배열
    private List<Object> notes;

    // 파일
    private List<AttachDto> files;

}