package com.kh.finalprj.vo.note;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "노트 댓글 정보 VO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NoteCommentDetailResponseVO {
	private int noteCommentNo;
	private int noteNo;
	private int projectMemberNo;
	private String noteCommentContent;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp noteCommentCtime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp noteCommentUtime;
	
	private int empNo;
	private String empName;
	private String empDeptNo;
	private String empPositionNo;
}
