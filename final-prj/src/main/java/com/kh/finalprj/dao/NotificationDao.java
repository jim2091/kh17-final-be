package com.kh.finalprj.dao;

import java.util.List;
import java.util.Map;

import com.kh.finalprj.dto.NotificationDto;

public interface NotificationDao {
	int sequence();
	void insert(NotificationDto notificationDto);
	List<NotificationDto> selectListByReceiver(int empNo);
	int countUnRead(int empNo);
	boolean markAsRead(int notificationNo, int empNo);
	boolean markAllAsRead(int empNo);
	boolean delete(int notificationNo, int empNo);
	List<NotificationDto>selectListByPage(Map<String,Object>params);
	int count(Map<String, Object>params);
}
