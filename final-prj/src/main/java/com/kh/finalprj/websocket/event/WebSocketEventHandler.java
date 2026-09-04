package com.kh.finalprj.websocket.event;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.kh.finalprj.dao.EmpDao;
import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.service.FlashService;

@Service
public class WebSocketEventHandler {
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	private FlashService flashService;
	
	@Autowired
	private EmpDao empDao;
	
	@EventListener
	public void enterOnline(
	        SessionConnectedEvent event) {

		//어떤 웹소켓 연결인지
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId();
	    
		//누가 연결했는지
		Principal principal = event.getUser();
	    String empNo = principal.getName();
	    
	    int empNumber = Integer.parseInt(empNo);
	    EmpDto empDto = empDao.selectOne(empNumber);
	    
	    flashService.enter(empDto, sessionId);
	    
	    List<EmpDto> onlineUsers = flashService.list();
	    
	    simpMessagingTemplate.convertAndSend("/public/onlineUsers", onlineUsers);
	    
	}
	@EventListener
	public void leaveOnline(SessionDisconnectEvent event) {
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = accessor.getSessionId();		

		Principal principal = event.getUser();
		String empNo = principal.getName();
		
		int empNumber = Integer.parseInt(empNo);
	    EmpDto empDto = empDao.selectOne(empNumber);
	    
	    flashService.leave(empDto, sessionId);
	    
	    List<EmpDto> onlineUsers = flashService.list();
	    
	    simpMessagingTemplate.convertAndSend("/public/onlineUsers", onlineUsers);
		
	}
	
}
