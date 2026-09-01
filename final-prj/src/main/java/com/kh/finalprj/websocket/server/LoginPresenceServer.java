package com.kh.finalprj.websocket.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.kh.finalprj.dao.EmpDao;
import com.kh.finalprj.service.FlashService;

//온라인/오프라인/자리비움 접속 상태 기능
//@Slf4j
@Controller
public class LoginPresenceServer {
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	private FlashService flashService;
	
//	@Autowired
//	private EmpDao empDao;
	

//	@MessageMapping("/basic")
//	@SendTo("/public/basic")
//	public void basic(String message) {
//		log.debug("메세지 수신 = {}", message);
//		return message;
//		simpMessagingTemplate.convertAndSend("/public/basic", message);
//	}
	

	
//	@MessageMapping("/heartbeat")
//	public void heartbeat() {
//		
//		simpMessagingTemplate.convertAndSend("/public/online", true);
//		
//		System.out.println("heartbeat 수신");

//	    String empName = principal.getName();
//
//	    System.out.println("heartbeat : " + empName);
//	}
	

	
	@MessageMapping("/onlineUsers")
	public void onlineUsers() {
	    simpMessagingTemplate.convertAndSend("/public/onlineUsers", flashService.list());
	    System.out.println("수신받음");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
