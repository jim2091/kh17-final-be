package com.kh.finalprj.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class AttachDto {
	private int attachNo;
	private int projectNo;
	private String attachName;
	private String attachType;
	private long attachSize;
	private String attachUploader;
	private String attachSource;
	private Timestamp attachCtime;
}
