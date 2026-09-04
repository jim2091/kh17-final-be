package com.kh.finalprj.vo.note;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kh.finalprj.dto.AttachDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="노트 상세 응답 VO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NoteDetailResponseVO {
	private int noteNo;
	private int projectNo;
	private String noteTitle;
	private String noteContent;
	private Integer assignedMemberNo;
	private String assignedMemberName;
	private String assignedMemberDept;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp noteCtime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private Timestamp noteUtime;
	private int noteWriterNo;
	private List<AttachDto> noteFiles;
}
