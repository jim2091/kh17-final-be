package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.NotificationDto;

public interface NotificationDao {
	int sequence();
	void insert(NotificationDto notificationDto);
	List<NotificationDto> selectListByReceiver(int empNo);
	int countUnRead(int empNo);
	boolean markAsRead(int notificationNo, int empNo);
	boolean markAllAsRead(int empNo);
	boolean delete(int notificationNo, int empNo);
}
