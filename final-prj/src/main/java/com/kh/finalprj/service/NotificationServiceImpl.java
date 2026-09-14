package com.kh.finalprj.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.NotificationDao;
import com.kh.finalprj.dto.NotificationDto;

@Service
public class NotificationServiceImpl implements NotificationService{
	
	@Autowired
	private NotificationDao notificationDao;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Override
	@Transactional
	public void send(NotificationDto notificationDto) {
		int nextNo = notificationDao.sequence();
		notificationDto.setNotificationNo(nextNo);
		notificationDto.setNotificationRead("N");
		notificationDao.insert(notificationDto);
		
		simpMessagingTemplate.convertAndSend(
			"/public/user/" + notificationDto.getNotificationReceiver() + "/notify", 
			notificationDto
		);
	}

	@Override
	public List<NotificationDto> getNotifications(int empNo) {
		return notificationDao.selectListByReceiver(empNo);
	}

	@Override
	public int getUnreadCount(int empNo) {
		return notificationDao.countUnRead(empNo);
	}

	@Override
	public Map<String, Object> getNotificationSummary(int empNo) {
		return Map.of(
			"list", notificationDao.selectListByReceiver(empNo),
			"unReadCount", notificationDao.countUnRead(empNo)
		);
	}

	@Override
	@Transactional
	public boolean markAsRead(int notificationNo, int empNo) {
		return notificationDao.markAsRead(notificationNo, empNo);
	}

	@Override
	@Transactional
	public boolean markAllAsRead(int empNo) {
		return notificationDao.markAllAsRead(empNo);
	}

}
