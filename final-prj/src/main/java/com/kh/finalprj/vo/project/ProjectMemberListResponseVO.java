package com.kh.finalprj.vo.project;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectMemberListResponseVO {

	private int projectMemberNo;
	private int projectNo;
	private int empNo;
	private String projectMemberRole;
	private String projectMemberJob;
	private Timestamp projectMemberCtime;
	
	private String empName;
}
