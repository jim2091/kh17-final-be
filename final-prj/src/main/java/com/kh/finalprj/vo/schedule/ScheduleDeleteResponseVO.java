package com.kh.finalprj.vo.schedule;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "일정 삭제 응답 데이터")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ScheduleDeleteResponseVO {
	private int scheduleNo;
}
