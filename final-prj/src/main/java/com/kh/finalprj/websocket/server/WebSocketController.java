package com.kh.finalprj.websocket.server;

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
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.message.MessageReadResponseVO;
import com.kh.finalprj.vo.message.MessageVO;
import com.kh.finalprj.websocket.vo.WebSocketRequestVO;
import com.kh.finalprj.websocket.vo.WebSocketResponseVO;

//실시간 메시지 송수신
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
		
		///System.out.println("===== WebSocket 채팅 수신 =====");
	    ///System.out.println("channelNo = " + channelNo);
		
		//[1] JWT에서 empNo 확인
		TokenParseResponseVO parseVO = 
				jwtService.parseAccessToken(jwt.getTokenValue());
		
		int empNo = parseVO.getEmpNo();
		
		///System.out.println("empNo = " + empNo);
		
		//[2] 사용자가 보낸 데이터
		WebSocketRequestVO request = message.getPayload();
		
		///System.out.println("content = " + request.getContent());
		
		//[3] channelNo로 projectNo 확인
	    int projectNo = channelDao.findProjectNo(channelNo);
		
	    //[4] projectNo와 empNo를 이용해 projectMemberNo 확인
		Integer projectMemberNo = 
				projectMemberDao.findProjectMemberNo(
						projectNo, empNo);
		
		//[5] empNo를 이용해 사원 정보 확인
		EmpDto empDto = empDao.selectOne(empNo);
	    
	    //[6] DB에 메세지 저장
	    MessageVO messageVO = MessageVO.builder()
	    			.channelNo(channelNo)
	    			.projectMemberNo(projectMemberNo)
	    			.content(request.getContent())
	    			.type("CHAT")
	    		.build();
	    
	    MessageVO save = messageService.add(messageVO);
	    
	    //[7] 새 메시지의 안 읽은 사람 수
	    int unreadCount = messageService.countUnread(
	    		save.getNo(), channelNo, projectMemberNo);
	    
	    //[8] 사용자에게 전달할 객체 (WebSocket 반환)
	    WebSocketResponseVO response = WebSocketResponseVO.builder()
	    			.no(save.getNo())
	    			.channelNo(channelNo)
	    			.projectMemberNo(projectMemberNo)
	    			.empNo(empNo)
	    			.senderName(empDto.getEmpName())
	    			.content(save.getContent())
	    			.type(save.getType())
	    			.ctime(save.getCtime())
	    			.utime(save.getUtime())
	    			.unreadCount(unreadCount)
	    		.build();
	    
	    //[8] 해당 채널 사용자들에게 전송
	    simpMessagingTemplate.convertAndSend(
	    		"/public/"+channelNo+"/chat",
	    		response
	    	);
	}
	
	
	//채널 메세지 읽음 처리
	@MessageMapping("/{channelNo}/read")
	public void read(
			@DestinationVariable int channelNo,
			@AuthenticationPrincipal Jwt jwt
		) {
		//[1] JWT에서 empNo 확인
		TokenParseResponseVO parseVO = 
				jwtService.parseAccessToken(jwt.getTokenValue());
		
		int empNo = parseVO.getEmpNo();
		
		//[2] 채널 메시지 읽음 처리 + 읽음 결과 조회
		MessageReadResponseVO response =
		        messageService.readChannelMessage(
		                channelNo, empNo);
		
		//[3] 해당 채널 사용자들에게 읽음 처리 알림
		simpMessagingTemplate.convertAndSend(
				"/public/"+channelNo+"/read",
				response	
			);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//웹소켓 테스트
	@MessageMapping("/test")
	public void test(String message) {
		System.out.println("수신 : " + message);
		
		simpMessagingTemplate.convertAndSend(
			"/public/test",
			message
		);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
