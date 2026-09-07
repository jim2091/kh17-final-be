package com.kh.finalprj.vo.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="회원 부서/직급 수정 요청VO")
@Data @JsonIgnoreProperties(ignoreUnknown= true)
public class EmpEditRequestVO {
	
	private int empNo;
	private int empDeptNo;
	private int empPositionNo;

}
