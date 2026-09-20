package com.kh.finalprj.websocket.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import com.kh.finalprj.dto.DmRoomDto;
import com.kh.finalprj.service.DmService;
import com.kh.finalprj.vo.dm.DmMessageVO;
import com.kh.finalprj.websocket.vo.WebSocketRequestVO;


@Controller
public class DmWebSocketController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private DmService dmService;

	//DM 메세지 전송
	//React → /app/dm/{roomNo}/chat
	@MessageMapping("/dm/{roomNo}/chat")
	public void chat(
			@DestinationVariable int roomNo,
			@AuthenticationPrincipal Jwt jwt,
			Message<WebSocketRequestVO> message
	) {
		//[1] 로그인 사용자 번호
		int empNo = Integer.parseInt(jwt.getSubject());

		//[2] 전송 내용
		WebSocketRequestVO request = message.getPayload();
		
		//[3] DM방 조회 + 참여 권한 확인
		DmRoomDto room = dmService.findRoom(roomNo, empNo);
		
		//[4] 상대방 번호
		int targetEmpNo = room.getDmRoomEmp1No() == empNo
							? room.getDmRoomEmp2No()
							: room.getDmRoomEmp1No();
		
		//[5] 메세지 저장
		DmMessageVO response = dmService.addMessage(
										roomNo,
										empNo,
										request.getContent()
								);

		//[6] 현재 DM방에 실시간 전송
		simpMessagingTemplate.convertAndSend("/public/dm/" + roomNo + "/chat", response);
		
		//[7] 보낸 사람 DM 목록 갱신 신호
		simpMessagingTemplate.convertAndSend("/public/dm/user/" + empNo + "/rooms", response);
		
		//[8] 상대방 DM 목록 갱신 신호
		simpMessagingTemplate.convertAndSend("/public/dm/user/" + targetEmpNo + "/rooms", response);
	}

}