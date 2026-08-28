package com.kh.finalprj.websocket.vo;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//서버가 사용자에게 보내는 데이터 (WebSocket 서버 → React)
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class WebSocketResponseVO {
	private int no;
	private int channelNo;
	private Integer projectMemberNo;
	private int empNo;
	private String senderName;
	private String content;
	private String type;
	private Timestamp ctime;
	private Timestamp utime;
	private int unreadCount;
}
