package com.kh.finalprj.vo.project;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectListResponseVO {

	private int projectNo;
	private String projectName;
	private String projectPurpose;
	private String projectVisibility;
	private String projectStatus;
	private Timestamp projectStart;
	private Timestamp projectDeadline;
	
	//사용자의 프로젝트 안에서 권한
	private String projectMemberRole;
}
