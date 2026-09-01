package com.kh.finalprj.vo.dept;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="부서 수정 요청 정보")
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class DeptChangeRequestVO {
	
	private int deptNo;
	private String deptName;
	private String deptInfo;
	private String deptBlock;

}
