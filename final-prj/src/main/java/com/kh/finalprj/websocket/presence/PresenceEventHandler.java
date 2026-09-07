package com.kh.finalprj.websocket.presence;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.kh.finalprj.websocket.presence.vo.PresenceResponseVO;

@Service
public class PresenceEventHandler {
	@Autowired
	private PresenceService presenceService;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@EventListener
	public void enter(SessionConnectedEvent event) {
		Principal principal = event.getUser();
		if(principal == null) return;
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		
		String sessionId = accessor.getSessionId();
		int empNo = Integer.parseInt(principal.getName());
		
		boolean isChanged = presenceService.enter(empNo, sessionId);
		
		if(isChanged) {
			PresenceStatus status = presenceService.getStatus(empNo);
			
			simpMessagingTemplate.convertAndSend(
					"/public/presence",
					PresenceResponseVO.builder()
					.empNo(empNo)
					.status(status)
					.build()
					);			
		}
	}
	
	@EventListener
	public void leave(SessionDisconnectEvent event) {
		Principal principal = event.getUser();
		if(principal == null) return;
		
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
		
		String sessionId = accessor.getSessionId();
		int empNo = Integer.parseInt(principal.getName());
		
		boolean isChanged = presenceService.leave(empNo, sessionId);
		
		if(isChanged) {
			simpMessagingTemplate.convertAndSend(
					"/public/presence",
					PresenceResponseVO.builder()
					.empNo(empNo)
					.status(PresenceStatus.OFFLINE)
					.build()
					);			
		}
	}
}
