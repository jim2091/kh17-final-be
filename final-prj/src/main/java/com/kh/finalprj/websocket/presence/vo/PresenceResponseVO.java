package com.kh.finalprj.websocket.presence.vo;

import com.kh.finalprj.websocket.presence.PresenceStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PresenceResponseVO {
	private int empNo;
	private String empName;
	private PresenceStatus status;
}
