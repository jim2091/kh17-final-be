package com.kh.finalprj.websocket.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

//사용자가 서버로 보내는 데이터 (React → WebSocket 서버)
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class WebSocketRequestVO {
	private String content;
}
