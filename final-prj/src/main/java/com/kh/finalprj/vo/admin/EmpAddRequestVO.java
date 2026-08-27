package com.kh.finalprj.vo.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="사용자 추가 요청 정보")
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class EmpAddRequestVO {
	
	private int empNo;
	private String empEmail;
	private String empName;
	private String empPassword;
	private int empDeptNo;
	private int empPositionNo;

}
