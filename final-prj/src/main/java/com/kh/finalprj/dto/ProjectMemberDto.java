package com.kh.finalprj.dto;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "프로젝트 멤버 DTO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectMemberDto {

	private int projectMemberNo;
	private int projectNo;
	private int empNo;
	private String projectMemberRole;
	private String projectMemberJob;
	private Timestamp projectMemberCtime;
}
