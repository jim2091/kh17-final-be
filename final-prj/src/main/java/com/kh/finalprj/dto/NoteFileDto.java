package com.kh.finalprj.dto;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="NOTE 첨부파일 DTO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NoteFileDto {
	private int noteNo;
	private int attachNo;
	private Timestamp noteFileCtime;
}
