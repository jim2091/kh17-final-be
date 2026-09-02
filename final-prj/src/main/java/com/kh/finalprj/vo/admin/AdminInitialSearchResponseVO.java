package com.kh.finalprj.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="사용자 초성검색(관리자용)응답 데이터")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AdminInitialSearchResponseVO {
	
	private int empNo;
	private String empName;
	private String deptName;
	private String positionName;
	private String empState;
	private String empEmail;

}
