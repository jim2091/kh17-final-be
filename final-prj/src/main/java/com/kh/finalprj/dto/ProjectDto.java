package com.kh.finalprj.dto;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "프로젝트 DTO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectDto {
	private int projectNo;
	private String projectName;
	private String projectPurpose;
	private String projectVisibility;
	private String projectStatus;
	private Timestamp projectStart, projectDeadline;
	private Timestamp projectCtime, projectUtime;
	
}
