package com.kh.finalprj.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachDto {

    private int attachNo;

    // DB attach 컬럼이 아님
    // project_file에서 조회한 프로젝트 번호를 담기 위한 DTO 필드
    private int projectNo;

    private String projectName;
    private String attachName;
    private String attachType;
    private long attachSize;
    private String attachUploader;
    private String attachSource;
    private Integer attachSourceNo;
    private Timestamp attachCtime;
    private String empName;
}