package com.kh.finalprj.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ProjectRecordSearchDto {

	// ========================================
	// 기록 번호
	// ========================================

	private int projectRecordNo;

	// ========================================
	// 프로젝트 번호
	// ========================================

	private int projectNo;

	// ========================================
	// 프로젝트 이름
	// ========================================

	private String projectName;

	// ========================================
	// 기록 작성자
	// ========================================

	private int projectRecordWriterNo;

	private int writerEmpNo;

	private String writerName;

	// ========================================
	// 기록 수정자
	// ========================================

	private Integer projectRecordModifierNo;

	// ========================================
	// 기록 타입
	// DECISION
	// ISSUE
	// DELIVERABLE
	// ETC
	// ========================================

	private String projectRecordType;

	// ========================================
	// 기록 제목
	// ========================================

	private String projectRecordTitle;

	// ========================================
	// 기록 내용
	// ========================================

	private String projectRecordContent;

	// ========================================
	// 생성일
	// ========================================

	private Timestamp projectRecordCtime;

	// ========================================
	// 수정일
	// ========================================

	private Timestamp projectRecordUtime;

	// ========================================
	// ISSUE 전용
	// ========================================

	private String issueStatus;

	private String issueResolution;

	private Timestamp issueResolvedAt;

}