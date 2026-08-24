package com.kh.finalprj.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DeptDto {
	
	private int deptNo;
	private String deptName;
	private String deptBlock;
	private String deptInfo;
	private Timestamp deptCtime;
	private Timestamp deptUtime;

}
