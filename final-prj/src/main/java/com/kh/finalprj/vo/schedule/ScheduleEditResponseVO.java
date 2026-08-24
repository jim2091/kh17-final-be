package com.kh.finalprj.vo.schedule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "일정 수정 응답 정보")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ScheduleEditResponseVO {
	private int scheduleNo;
}
