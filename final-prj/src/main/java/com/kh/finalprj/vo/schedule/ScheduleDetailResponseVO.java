package com.kh.finalprj.vo.schedule;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "일정 상세 조회 데이터")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ScheduleDetailResponseVO {
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
