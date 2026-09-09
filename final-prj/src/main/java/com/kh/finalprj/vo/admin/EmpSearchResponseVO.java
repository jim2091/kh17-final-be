package com.kh.finalprj.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="키워드로 사용자 목록 검색하기 응답 VO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EmpSearchResponseVO {
	
	private int empNo;
	private String empName;
	private String deptName;
	private String positionName;
	private String empLevel;
	private String empEmail;
	private String empContact;
	private String empAddress1;

}
