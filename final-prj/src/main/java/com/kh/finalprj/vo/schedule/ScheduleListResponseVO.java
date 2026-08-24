package com.kh.finalprj.vo.schedule;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "일정 목록 조회 데이터")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ScheduleListResponseVO {
	List<ScheduleEventVO> scheduleList;
}
