package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.dto.ProjectDto;
import com.kh.finalprj.dto.TaskDto;

public interface SearchDao {

    // 사용자 검색
    List<EmpDto> searchMembers(String keyword);

    // 프로젝트 검색
    List<ProjectDto> searchProjects(String keyword, int empNo);

    // 업무 검색
    List<TaskDto> searchTasks(String keyword);

    // 파일 검색
    List<AttachDto> searchFiles(String keyword);

}