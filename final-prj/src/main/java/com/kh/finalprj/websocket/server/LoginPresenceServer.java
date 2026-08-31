package com.kh.finalprj.websocket.server;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import lombok.extern.slf4j.Slf4j;

//온라인/오프라인/자리비움 접속 상태 기능
@Slf4j
@Controller
public class LoginPresenceServer {
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	

	@MessageMapping("/basic")
//	@SendTo("/public/basic")
	public void basic(String message) {
		log.debug("메세지 수신 = {}", message);
//		return message;
		simpMessagingTemplate.convertAndSend("/public/basic", message);
	}
	
//	@EventListener
//	public void handleWebSocketConnectListener(
//	        SessionConnectEvent event) {
//
//	    StompHeaderAccessor accessor =
//	            StompHeaderAccessor.wrap(event.getMessage());
//
//	    Principal principal = accessor.getUser();
//
//	    System.out.println("principal"+principal);
//	    
//	    String empName = principal.getName();
//	    
//	    System.out.println("principal : " + empName);
//	    
//	}
	
	@MessageMapping("/heartbeat")
	public void heartbeat() {
		
		simpMessagingTemplate.convertAndSend("/public/online", true);
		
		System.out.println("heartbeat 수신");

//	    String empName = principal.getName();
//
//	    System.out.println("heartbeat : " + empName);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
