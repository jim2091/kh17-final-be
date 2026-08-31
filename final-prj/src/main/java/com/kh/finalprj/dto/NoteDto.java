package com.kh.finalprj.dto;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "노트 DTO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NoteDto {
	private int noteNo;
	private int projectNo;
	private int noteWriterNo;
	private String noteTitle;
	private String noteContent;
	private Timestamp noteCtime;
	private Timestamp noteUtime;
}
