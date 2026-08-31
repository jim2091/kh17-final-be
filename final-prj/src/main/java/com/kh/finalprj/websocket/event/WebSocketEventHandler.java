package com.kh.finalprj.websocket.event;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
	

	
	@EventListener
	public void subscribe(SessionSubscribeEvent event) {
		
//		System.out.println("채널을 구독하였습니다");
		SimpMessageHeaderAccessor headerAccessor = 
				SimpMessageHeaderAccessor.wrap(event.getMessage());
		
		String destination = headerAccessor.getDestination();
		
		if(destination.equals("/public/users")) {
			simpMessagingTemplate.convertAndSend("/public/onlineUsers", flashService.list() );
		}
//		System.out.println("구독명단 : "+ flashService.list());
		
	}

}
