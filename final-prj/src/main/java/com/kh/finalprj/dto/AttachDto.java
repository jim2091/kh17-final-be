package com.kh.finalprj.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AttachDto {
	private int attachNo;
	private String projectName;
	private String attachName;
	private String attachType;
	private long attachSize;
	private String attachUploader;
	private String attachSource;
	private Timestamp attachCtime;
	private String empName;
}
