package com.kh.finalprj.vo.project;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "프로젝트멤버 생성 객체")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectMemberCreateRequestVO {

	@NotNull
	private int projectNo;
	@NotNull
	private int empNo;
	private String projectMemberRole;
	private String projectMemberJob;
}
