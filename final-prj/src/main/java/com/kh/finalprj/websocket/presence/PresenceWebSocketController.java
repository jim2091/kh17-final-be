package com.kh.finalprj.websocket.presence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import com.kh.finalprj.websocket.presence.vo.PresenceChangeRequestVO;
import com.kh.finalprj.websocket.presence.vo.PresenceResponseVO;

@Controller
public class PresenceWebSocketController {

	@Autowired
	private PresenceService presenceService;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@MessageMapping("/presence/status")
	public void changeStatus(
			 @AuthenticationPrincipal Jwt jwt,
			 Message<PresenceChangeRequestVO> message) {
		
		int empNo = Integer.parseInt(jwt.getSubject());
		
		PresenceChangeRequestVO request = message.getPayload();
		
		boolean isChanged = presenceService.changeStatus(empNo, request.getStatus());
		
		if(!isChanged) return;
		
		PresenceResponseVO response = PresenceResponseVO.builder()
				.empNo(empNo)
				.status(request.getStatus())
				.build();
		
		simpMessagingTemplate.convertAndSend(
				"/public/presence",
				response
		);
		
	}
}
