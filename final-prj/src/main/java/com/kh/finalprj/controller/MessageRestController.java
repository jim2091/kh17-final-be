package com.kh.finalprj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.service.JwtService;
import com.kh.finalprj.service.MessageService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.message.MessageUpdateRequestVO;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "메세지 API")
@RestController
@RequestMapping("/api/message")
public class MessageRestController {
	@Autowired
	private MessageService messageService;
	@Autowired
	private JwtService jwtService;
	
	@ApiResponse(responseCode = "200", description = "메세지 삭제 성공")
	@DeleteMapping("/{chatMessageNo}")
	public void delete(
		@PathVariable int chatMessageNo,
		@AuthenticationPrincipal Jwt jwt
		) {
		
		//[1] JWT에서 empNo 확인
		TokenParseResponseVO parseVO = 
			jwtService.parseAccessToken(jwt.getTokenValue());
		
		int empNo = parseVO.getEmpNo();
		
		//[2] 메세지 삭제
		messageService.delete(chatMessageNo, empNo);
	}
	
	@ApiResponse(responseCode = "200", description = "메세지 수정 성공")
	@PutMapping("/{chatMessageNo}")
	public void update(
			@PathVariable int chatMessageNo,
			@RequestBody MessageUpdateRequestVO request,
			@AuthenticationPrincipal Jwt jwt
		) {
		//[1] JWT에서 empNo 확인
		TokenParseResponseVO parseVO = 
			jwtService.parseAccessToken(jwt.getTokenValue());
		
		int empNo = parseVO.getEmpNo();
		
		//[2] 메세지 수정
		messageService.update(chatMessageNo, request, empNo);
	}
}
