package com.kh.finalprj.dto;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "일정 DTO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ScheduleDto {
	private int scheduleNo;
	private int projectNo;
	private int scheduleWriterNo;
	private String scheduleTitle;
	private String scheduleContent;
	private Timestamp scheduleStart;
	private Timestamp scheduleEnd;
	private String schedulePlace;
	private Timestamp scheduleCtime;
	private Timestamp scheduleUtime;
}
