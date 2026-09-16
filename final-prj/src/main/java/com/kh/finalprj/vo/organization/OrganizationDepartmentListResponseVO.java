package com.kh.finalprj.vo.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "조직 부서 목록 응답")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OrganizationDepartmentListResponseVO {

	private int deptNo;
	private String deptName;
	private String deptInfo;

	private int memberCount;

}