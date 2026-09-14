package com.kh.finalprj.vo.record;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "프로젝트 record 요약 응답 데이터")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectRecordSummaryResponseVO {
	int totalCount;
	
	private int decisionCount;
	private int issueCount;
	private int deliverableCount;
	private int etcCount;
	
	private int openIssueCount;
	private int resolvedIssueCount;
	
	private Integer latestRecordNo;
	private String latestRecordTitle;
	private Timestamp latestRecordAt;
}
