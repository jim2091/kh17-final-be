package com.kh.finalprj.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectHistoryResponseDto {

    // 사용자 정보
    private int empNo;
    private String empName;
    private String empEmail;
    private String deptName;
    private String positionName;
    private String empContact;

    // 프로젝트 참여 이력
    private List<ProjectHistoryDto> projects;
}