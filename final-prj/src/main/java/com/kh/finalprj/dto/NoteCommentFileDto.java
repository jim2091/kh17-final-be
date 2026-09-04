package com.kh.finalprj.dto;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="노트 댓글 첨부파일 DTO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NoteCommentFileDto {
	private int noteCommentNo;
	private int attachNo;
	private Timestamp noteCommentFileCtime;
}
