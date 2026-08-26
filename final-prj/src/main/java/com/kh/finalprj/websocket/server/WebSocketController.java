package com.kh.finalprj.websocket.server;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import com.kh.finalprj.dao.ChannelDao;
import com.kh.finalprj.dao.EmpDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.service.JwtService;
import com.kh.finalprj.service.MessageService;
import com.kh.finalprj.vo.channel.MessageVO;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.websocket.vo.WebSocketRequestVO;
import com.kh.finalprj.websocket.vo.WebSocketResponseVO;

@Controller
public class WebSocketController {
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	private MessageService messageService;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private ProjectMemberDao projectMemberDao;
	@Autowired
	private EmpDao empDao;

	//주소 : /app/{channleNo}/chat
	@MessageMapping("/{channelNo}/chat")
	public void chat(
			@DestinationVariable int channelNo,
			@AuthenticationPrincipal Jwt jwt,
			Message<WebSocketRequestVO> message
		) {
		
		//System.out.println("===== WebSocket 채팅 수신 =====");
	    //System.out.println("channelNo = " + channelNo);
		
		//[1] JWT에서 empNo 확인
		TokenParseResponseVO parseVO = 
				jwtService.parseAccessToken(jwt.getTokenValue());
		
		int empNo = parseVO.getEmpNo();
		
		//System.out.println("empNo = " + empNo);
		
		//[2] 사용자가 보낸 데이터
		WebSocketRequestVO request = message.getPayload();
		
		//System.out.println("content = " + request.getContent());
		
		//[3] channelNo로 projectNo 확인
	    int projectNo = channelDao.findProjectNo(channelNo);
		
	    //[4] projectNo와 empNo를 이용해 projectMemberNo 확인
		Integer projectMemberNo = 
				projectMemberDao.findProjectMemberNo(
						projectNo, empNo);
		
		//[5] empNo를 이용해 사원 정보 확인
		EmpDto empDto = empDao.selectOne(empNo);
	    
	    //[6] DB에 저장
	    MessageVO messageVO = MessageVO.builder()
	    			.channelNo(channelNo)
	    			.projectMemberNo(projectMemberNo)
	    			.content(request.getContent())
	    			.type("CHAT")
	    		.build();
	    
	    MessageVO save = messageService.add(messageVO);
	    
	    //[7] 사용자에게 전달할 객체 (WebSocket 반환)
	    WebSocketResponseVO response = WebSocketResponseVO.builder()
	    			.no(save.getNo())
	    			.channelNo(channelNo)
	    			.projectMemberNo(projectMemberNo)
	    			.empNo(empNo)
	    			.senderName(empDto.getEmpName())
	    			.content(save.getContent())
	    			.type(save.getType())
	    			.time(LocalDateTime.now())
	    		.build();
	    
	    //[8] 해당 채널 사용자들에게 전송
	    simpMessagingTemplate.convertAndSend(
	    		"/public/"+channelNo+"/chat",
	    		response
	    	);
	}
	
	
	
	
	
	
	
	@MessageMapping("/test")
	public void test(String message) {
		System.out.println("수신 : " + message);
		
		simpMessagingTemplate.convertAndSend(
			"/public/test",
			message
		);
	}
}
