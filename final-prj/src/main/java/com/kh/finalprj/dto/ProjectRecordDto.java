package com.kh.finalprj.dto;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "프로젝트 record Dto")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectRecordDto {
	private int projectRecordNo;
	private int projectNo;
	private int projectRecordWriterNo;
	private Integer projectRecordModifierNo;
	private String projectRecordType;
	private String projectRecordTitle;
	private String projectRecordContent;
	private Timestamp projectRecordCtime;
	private Timestamp projectRecordUtime;
}
