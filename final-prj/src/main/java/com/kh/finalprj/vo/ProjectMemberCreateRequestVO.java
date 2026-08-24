package com.kh.finalprj.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "프로젝트멤버 생성 객체")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectMemberCreateRequestVO {

	private int projectNo;
	private int empNo;
	private String projectMemberRole;
	private String projectMemberJob;
}
