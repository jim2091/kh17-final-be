package com.kh.finalprj.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ProjectHistoryDto {

    private int projectNo;
    private String projectName;

    private String projectMemberRole;
    private String projectMemberJob;
    private Timestamp projectMemberCtime;

    // 프로젝트 상태
    private String projectStatus;
}