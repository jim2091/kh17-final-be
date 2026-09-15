package com.kh.finalprj.service;

import java.util.List;
import java.util.Map;

import com.kh.finalprj.dto.NotificationDto;

public interface NotificationService {
	void send(NotificationDto notificationDto);
	List<NotificationDto> getNotifications(int empNo);
	int getUnreadCount(int empNo);
	Map<String, Object> getNotificationSummary(int empNo);
	boolean markAsRead(int notificationNo, int empNo);
	boolean markAllAsRead(int empNo);
	void sendDeadlineNotifications();
}
