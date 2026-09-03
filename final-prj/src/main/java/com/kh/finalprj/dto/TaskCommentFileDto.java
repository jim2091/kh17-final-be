package com.kh.finalprj.dto;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="업무 댓글 첨부파일 DTO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TaskCommentFileDto {
	private int taskCommentNo;
	private int attachNo;
	private Timestamp taskCommentFileCtime;
}
