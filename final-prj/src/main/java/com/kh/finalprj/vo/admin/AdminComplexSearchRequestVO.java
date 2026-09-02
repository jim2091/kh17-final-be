package com.kh.finalprj.vo.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="사용자 복합검색(관리자용) 요청 데이터")
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class AdminComplexSearchRequestVO {
	
//	private String deptName;
//	private String positionName;
//	private String empState;
//	private String empName;
	private String keyword;

}
