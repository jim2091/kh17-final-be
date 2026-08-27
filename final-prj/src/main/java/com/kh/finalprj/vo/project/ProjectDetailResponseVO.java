package com.kh.finalprj.vo.project;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "프로젝트 상세조회 응답 객체")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectDetailResponseVO {

	private int projectNo;
	private String projectName;
	private String projectPurpose;
	private String projectVisibility;
	private String projectStatus;
	private Timestamp projectStart,projectDeadline;

	private int projectMemberNo;
	//현재 로그인 사용자의 프로젝트 역할
	private String projectMemberRole;
}
