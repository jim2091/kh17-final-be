package com.kh.finalprj.vo.project;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "프로젝트 수정 객체")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectUpdateRequestVO {

	@NotNull
	private String projectName;
	@NotNull
	private String projectPurpose;
	@NotNull
	private String projectVisibility;
	
	private Timestamp projectStart;
	private Timestamp projectDeadline;
}
