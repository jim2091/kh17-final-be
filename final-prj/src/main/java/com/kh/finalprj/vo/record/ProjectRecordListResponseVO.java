package com.kh.finalprj.vo.record;

import java.sql.Timestamp;
import java.util.List;

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
	
	//목록용 본문 일부
	private String projectRecordContentPreview;
	
	//작성자
	private int projectRecordWriterNo;
	private String projectRecordWriterName;
	
	//최종 수정자
	private Integer projectRecordModifierNo;
	private String projectRecordModifierName;
	
	private Timestamp projectRecordCtime;
	private Timestamp projectRecordUtime;
	
	//ISSUE일때만
	private String projectRecordIssueStatus;
	
	//ISSUE 해결 내용 일부
	private String projectRecordIssueResolutionPreview;
	
	//ISSUE 해결 시간
	private Timestamp projectRecordIssueResolvedAt;
	
	//연관 원본
	//목록에서는 최대 3개만 담을 예정
	private List<ProjectRecordRelatedResponseVO> relatedList;
	
	//실제 연결된 원본 전체 개수
	private int relatedCount;
}
