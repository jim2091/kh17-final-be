package com.kh.finalprj.websocket.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@MessageMapping("/test")
	public void test(String message) {
		System.out.println("수신 : " + message);
		
		simpMessagingTemplate.convertAndSend(
			"/public/test",
			message
		);
	}
}
