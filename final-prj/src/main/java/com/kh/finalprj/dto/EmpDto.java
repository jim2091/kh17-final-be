package com.kh.finalprj.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EmpDto {
	
	private int empNo;
	private String empPassword;
	private String empName;
	private String empEmail;
	private int empDeptNo;
	private int empPositionNo;
	private String empLevel;
	private String empState;
	private String empPresence;
	private String empBirth;
	private String empContact;
	private String empPost;
	private String empAddress1;
	private String empAddress2;
	private Timestamp empCtime;
	private Timestamp empUtime;
	

}
