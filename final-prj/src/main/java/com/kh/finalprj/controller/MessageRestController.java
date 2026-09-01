package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.error.GetOutException;
import com.kh.finalprj.service.JwtService;
import com.kh.finalprj.service.MessageService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.message.ChannelMessageRequestVO;
import com.kh.finalprj.vo.message.ChannelMessageResponseVO;
import com.kh.finalprj.vo.message.MessageDeleteResponseVO;
import com.kh.finalprj.vo.message.MessageTargetVO;
import com.kh.finalprj.vo.message.MessageUnreadChannelVO;
import com.kh.finalprj.vo.message.MessageUpdateRequestVO;
import com.kh.finalprj.vo.message.MessageUpdateResponseVO;
import com.kh.finalprj.vo.message.MessageVO;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

//메시지 작업 (수정/삭제 + 읽음 처리)
@Tag(name = "메세지 API")
@RestController
@RequestMapping("/api/message")
public class MessageRestController {
	@Autowired
	private MessageService messageService;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	private ProjectMemberDao projectMemberDao;
	
	
	//메세지 조회
	@ApiResponse(responseCode = "200", description = "메세지 조회 성공")
	@PostMapping("/channel/{channelNo}")
	public ChannelMessageResponseVO messages(
			@PathVariable int channelNo,
			@CurrentUser TokenParseResponseVO parseVO,
			@Valid @RequestBody ChannelMessageRequestVO request
		) {
		return messageService.selectList(
				channelNo, parseVO.getEmpNo(), request);
	}
	
	//메세지 삭제
	@ApiResponse(responseCode = "200", description = "메세지 삭제 성공")
	@DeleteMapping("/{chatMessageNo}")
	public void delete(
		@PathVariable int chatMessageNo,
		@CurrentUser TokenParseResponseVO parseVO
		) {
		
		//[1] empNo 확인
		int empNo = parseVO.getEmpNo();
		
		//[2] 삭제 대상 정보 확보
		MessageTargetVO target = 
				messageService.delete(chatMessageNo, empNo);
		
		//[3] Websocket으로 보낼 응답 생성
		MessageDeleteResponseVO response = 
				MessageDeleteResponseVO.builder()
						.messageNo(chatMessageNo)
						.channelNo(target.getChannelNo())
						.deleted("Y")
				.build();
		
		//[4] 해당 채널 사용자들에게 삭제 알림
		simpMessagingTemplate.convertAndSend(
				"/public/"+target.getChannelNo()+"/delete",
				response
			);
	}
	
	
	//메세지 수정
	@ApiResponse(responseCode = "200", description = "메세지 수정 성공")
	@PutMapping("/{chatMessageNo}")
	public void update(
			@PathVariable int chatMessageNo,
			@RequestBody MessageUpdateRequestVO request,
			@CurrentUser TokenParseResponseVO parseVO
		) {
		//[1] empNo 확인
		int empNo = parseVO.getEmpNo();
		
		//[2] 메세지 수정
		 MessageVO message = messageService.update(
				 chatMessageNo, request, empNo);
		
		//[3] Websocket으로 보낼 응답 생성
		MessageUpdateResponseVO response = 
				MessageUpdateResponseVO.builder()
						.messageNo(message.getNo())
						.channelNo(message.getChannelNo())
						.content(message.getContent())
						.utime(message.getUtime())
				.build();
		
		//[4] 해당 채널 사용자들에게 수정 알림
		simpMessagingTemplate.convertAndSend(
				"/public/"+message.getChannelNo()+"/update",
				response
			);
	}
	
	@ApiResponse(responseCode = "200", description = "안 읽은 메세지 읽음 처리 성공")
	@PostMapping("/{channelNo}/read")
	public void readChannelMessage(
			@PathVariable int channelNo,
			@CurrentUser TokenParseResponseVO parseVO
		) {
		//[1] empNo 확인
		int empNo = parseVO.getEmpNo();
		
		//[2] 안 읽은 메세지 읽음 처리
		messageService.readChannelMessage(channelNo, empNo);
	}
	
	
	//채널별 내가 안 읽은 메세지 수 조회
	@ApiResponse(responseCode = "200", description = "채널별 안 읽은 메세지 개수 표시 성공")
	@GetMapping("/project/{projectNo}/unread")
	public List<MessageUnreadChannelVO> selectChannelUnreadCount(
	        @PathVariable int projectNo,
	        @CurrentUser TokenParseResponseVO parseVO
	) {

	    // [1] empNo 확인
	    int empNo = parseVO.getEmpNo();

	    // [2] 현재 사용자의 projectMemberNo 조회
	    Integer projectMemberNo =
	            projectMemberDao.findProjectMemberNo(projectNo, empNo);

	    // [3] 프로젝트 참여자가 아니면 예외
	    if(projectMemberNo == null) {
	        throw new GetOutException();
	    }

	    // [4] 채널별 안 읽은 메시지 수 조회
	    return messageService.selectChannelUnreadCount(
	            projectNo, projectMemberNo
	    );
	}
}
