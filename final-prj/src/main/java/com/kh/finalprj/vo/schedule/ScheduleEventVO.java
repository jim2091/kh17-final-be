package com.kh.finalprj.vo.schedule;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "일정 목록 내 일정 1개 정보")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ScheduleEventVO {
	private int scheduleNo;
	private int scheduleWriterNo;
	private String scheduleTitle;
	private Timestamp scheduleStart;
	private Timestamp scheduleEnd;
	private String schedulePlace;
}
