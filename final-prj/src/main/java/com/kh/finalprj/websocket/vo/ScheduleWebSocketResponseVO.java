package com.kh.finalprj.websocket.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ScheduleWebSocketResponseVO {
	//add, edit, delete 세가지로 생각
	private String type;
	private int projectNo;
	private int scheduleNo;
}
