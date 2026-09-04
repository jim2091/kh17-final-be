package com.kh.finalprj.dto;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "노트 댓글 DTO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NoteCommentDto {
	private int noteCommentNo;
	private int noteNo;
	private int projectMemberNo;
	private String noteCommentContent;
	private Timestamp noteCommentCtime;
	private Timestamp noteCommentUtime;
	
	private int empNo;
	private String empName;
	private String empDeptNo;
	private String empPositionNo;
}
