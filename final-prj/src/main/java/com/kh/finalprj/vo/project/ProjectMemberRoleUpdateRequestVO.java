package com.kh.finalprj.vo.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Schema(name = "프로젝트 역할 변경VO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectMemberRoleUpdateRequestVO {

	private String projectMemberRole;
}
