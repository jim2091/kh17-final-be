package com.kh.finalprj.vo.record;

import java.sql.Timestamp;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "프로젝트 record 상세 응답 데이터")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectRecordDetailResponseVO {
	private int projectRecordNo;
	private int projectNo;
	private String projectRecordType;
	private String projectRecordTitle;
	private String projectRecordContent;
	private int projectRecordWriterNo;
	private String projectRecordWriterName;
	private Integer projectRecordModifierNo;
	private String projectRecordModifierName;
	private Timestamp projectRecordCtime;
	private Timestamp projectRecordUtime;
	
	//ISSUE일때만
	private String projectRecordIssueStatus;
	private String projectRecordIssueResolution;
	private Timestamp projectRecordIssueResolvedAt;
	
	//연관 원본
	private List<ProjectRecordRelatedResponseVO> relatedList;
}
