package com.kh.finalprj.websocket.interceptor;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.kh.finalprj.error.GetOutException;
import com.kh.finalprj.error.WhoAreYouException;
import com.kh.finalprj.service.DmService;


@Component
public class DmSubscriptionInterceptor implements ChannelInterceptor {

	@Autowired
	private DmService dmService;

	@Override
	public Message<?> preSend(
			Message<?> message,
			MessageChannel channel
	) {
		StompHeaderAccessor accessor =
				MessageHeaderAccessor.getAccessor(
						message,
						StompHeaderAccessor.class
				);

		if(accessor == null) return message;

		//SUBSCRIBE만 검사
		if(
			accessor.getCommand()
			!= StompCommand.SUBSCRIBE
		) {
			return message;
		}

		String destination = accessor.getDestination();

		if(destination == null) return message;

		//DM topic이 아니면 검사하지 않음
		boolean dmChatTopic = destination.matches("/public/dm/\\d+/chat$");
		
		boolean dmRoomListTopic = destination.matches("/public/dm/user/\\d+/rooms$");
		
		if(dmChatTopic == false && dmRoomListTopic == false) {
			return message;
		}

		//로그인 사용자 확인
		Principal principal = accessor.getUser();

		if(principal == null) 
			throw new WhoAreYouException();

		int empNo =	Integer.parseInt(principal.getName());

		//개인 DM 목록 갱신 topic
		if(dmRoomListTopic) {
			String[] parts = destination.split("/");
			
			int targetEmpNo = Integer.parseInt(parts[4]);
			
			//본인 DM 목록 topic만 구독 가능
			if(targetEmpNo != empNo)
				throw new GetOutException();
			
			return message;
		}
		
		//DM 채팅방 topic
		if(dmChatTopic) {
			String[] parts = destination.split("/");
			
			int roomNo = Integer.parseInt(parts[3]);
			
			//방 존재 여부 + 현재 사용자 참여 여부 검사
			dmService.findRoom(roomNo, empNo);
			
			return message;
		}

		return message;
	}

}