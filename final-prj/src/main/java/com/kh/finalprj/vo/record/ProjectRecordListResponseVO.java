package com.kh.finalprj.vo.record;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "프로젝트 record 목록 응답 데이터")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectRecordListResponseVO {
	private int projectRecordNo;
	private String projectRecordType;
	private String projectRecordTitle;
	private int projectRecordWriterNo;
	private String projectRecordWriterName;
	private Timestamp projectRecordCtime;
	private Timestamp projectRecordUtime;
	
	//ISSUE일때만
	private String projectRecordIssueStatus;
}
