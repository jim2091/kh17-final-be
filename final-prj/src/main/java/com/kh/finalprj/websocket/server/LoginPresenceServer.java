package com.kh.finalprj.websocket.server;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.kh.finalprj.dao.EmpDao;
import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.service.FlashService;

import lombok.extern.slf4j.Slf4j;

//온라인/오프라인/자리비움 접속 상태 기능
@Slf4j
@Controller
public class LoginPresenceServer {
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	private FlashService flashService;
	
	@Autowired
	private EmpDao empDao;
	

	@MessageMapping("/basic")
//	@SendTo("/public/basic")
	public void basic(String message) {
		log.debug("메세지 수신 = {}", message);
//		return message;
		simpMessagingTemplate.convertAndSend("/public/basic", message);
	}
	
	@EventListener
	public void enterOnline(
	        SessionConnectedEvent event) {

	   

	    Principal principal = event.getUser();

//	    System.out.println("principal : "+principal);
	    
	    String empNo = principal.getName();
	    
//	    System.out.println("로그인 : " + empNo);
	    
	    int empNumber = Integer.parseInt(empNo);

	    EmpDto empDto = empDao.selectOne(empNumber);
	    
//	    System.out.println("사용자 정보 : "+ empDto);
	    
	    
	    flashService.enter(empDto);
	    
	    
	    List<EmpDto> onlineUsers = flashService.list();
	    
	    simpMessagingTemplate.convertAndSend("/public/onlineUsers", onlineUsers);
	    
	}
	@EventListener
	public void leaveOnline(SessionDisconnectEvent event) {
		Principal principal = event.getUser();
		
		String empNo = principal.getName();
		int empNumber = Integer.parseInt(empNo);

	    EmpDto empDto = empDao.selectOne(empNumber);
		
	    
//	    System.out.println("로그아웃 : "+ empNo);
	    
	    flashService.leave(empDto);
	    
	    List<EmpDto> onlineUsers = flashService.list();
	    
	    simpMessagingTemplate.convertAndSend("/public/onlineUsers", onlineUsers);
		
	}
	
	@MessageMapping("/heartbeat")
	public void heartbeat() {
		
		simpMessagingTemplate.convertAndSend("/public/online", true);
		
		System.out.println("heartbeat 수신");

//	    String empName = principal.getName();
//
//	    System.out.println("heartbeat : " + empName);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
