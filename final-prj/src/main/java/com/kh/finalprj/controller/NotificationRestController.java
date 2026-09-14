package com.kh.finalprj.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.service.NotificationService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "알림 API")
@CommonsApiResponse
@RestController
@RequestMapping("/api/notification")
public class NotificationRestController {

	@Autowired
	private NotificationService notificationService;
	
	//내 알림 목록 확인 (확인 안한거)
	@ApiResponse(responseCode = "200", description = "알람 조회 성공")
	@GetMapping(value = "/", produces = "application/json")
	public Map<String, Object> getMyNotifications(@CurrentUser TokenParseResponseVO parseVO) {
		int loginEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 0;
		return notificationService.getNotificationSummary(loginEmpNo);
	}
	
	//하나 읽음처리
	@ApiResponse(responseCode = "200", description = "하나 읽음처리 성공")
	@PatchMapping(value = "/{notificationNo}/read", produces = "application/json")
	public boolean readNotification(
			@PathVariable int notificationNo,
			@CurrentUser TokenParseResponseVO parseVO) {
		int loginEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 0;
		return notificationService.markAsRead(notificationNo, loginEmpNo);
	}
	
	//전체 읽음처리
	@ApiResponse(responseCode = "200", description = "전체 읽음처리 성공")
	@PatchMapping(value = "/read-all", produces = "application/json")
	public boolean readAllNotifications(@CurrentUser TokenParseResponseVO parseVO) {
		int loginEmpNo = (parseVO != null) ? parseVO.getEmpNo() : 0;
		return notificationService.markAllAsRead(loginEmpNo);
	}
	
}
