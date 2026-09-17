package com.kh.finalprj.websocket.interceptor;

import java.nio.file.AccessDeniedException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.kh.finalprj.dao.ChannelDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.error.GetOutException;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.error.WhoAreYouException;

//스프링 시큐리티를 통해 로그인한 사람인지는 검사되고 있지만
//그 사람이 해당 프로젝트 채널을 구독해도 되는지는 검사하지 않음
//즉 로그인한 사용자가 개발자도구 등으로 다른 프로젝트의 채널번호를 알아내서
//직접 해당 채팅 채널을 구독하면 참여하지 않은 프로젝트의 메세지를 받을 수 있음

@Component
public class ChatSubscriptionInterceptor implements ChannelInterceptor{
	
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private ProjectMemberDao projectMemberDao;
	
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
					message,
					StompHeaderAccessor.class
				);
		
		if(accessor == null) {
			return message;
		}
		
		//SUBSCRIBE 명령만 검사
		if(accessor.getCommand() != StompCommand.SUBSCRIBE) {
			return message;
		}
		
		String destination = accessor.getDestination();
		
		if(destination == null) {
			return message;
		}
		
		Principal principal = accessor.getUser();
		
		if(principal == null) {
			throw new WhoAreYouException();
		}
		
		int empNo = Integer.parseInt(principal.getName());
		
		//채널 단위 채팅 topic
		// /public/{chanelNo}/chat
		// /public/{chanelNo}/read
		// /public/{chanelNo}/update
		// /public/{chanelNo}/delete
		if(destination.matches(
			"^/public/\\d+(chat|read|update|delete)$"
		)) {
			String[] parts = destination.split("/");
			
			int channelNo = Integer.parseInt(parts[2]);
			
			Integer projectNo = channelDao.findProjectNo(channelNo);
			
			if(projectNo == null) {
				throw new TargetNotfoundException();
			}
			
			checkProjectMember(projectNo, empNo);
		}
		
		return message;
	}
	
	//프로젝트 멤버 여부 확인
	private void checkProjectMember(int projectNo, int empNo) {
		Integer projectMemberNo = projectMemberDao.findProjectMemberNo(projectNo, empNo);
		
		if(projectMemberNo == null) {
			throw new GetOutException();
		}
	}
}
