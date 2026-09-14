package com.kh.finalprj.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.NotificationDao;
import com.kh.finalprj.dao.ScheduleDao;
import com.kh.finalprj.dto.NotificationDto;
import com.kh.finalprj.dto.ScheduleDto;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	@Autowired
	private NotificationDao notificationDao;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	private ScheduleDao scheduleDao;

	@Override
	@Transactional
	public void send(NotificationDto notificationDto) {
		int nextNo = notificationDao.sequence();
		notificationDto.setNotificationNo(nextNo);
		notificationDto.setNotificationRead("N");
		notificationDao.insert(notificationDto);
		
		// 실시간 웹소켓 푸시 전송 (수신자 사번 기준 채널)
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
	
	@Scheduled(cron = "0 0 8 * * *")
	@Transactional
	@Override
	public void sendDeadlineNotifications() {
		List<ScheduleDto> urgentSchedules = scheduleDao.selectTodayDeadlineSchedules();
		
		if (urgentSchedules == null || urgentSchedules.isEmpty()) {
			return;
		}
		
		for (ScheduleDto schedule : urgentSchedules) {
			String content = "오늘 마감인 일정 '" + schedule.getScheduleTitle() + "'이 있습니다.";
			String url = "/projects/" + schedule.getProjectNo() + "/schedule";
			
			NotificationDto notificationDto = NotificationDto.builder()
					.notificationReceiver(schedule.getScheduleWriterNo()) // 수신자 사번 (emp_no)
					.projectNo(schedule.getProjectNo())
					.notificationType("SCHEDULE_DEADLINE")
					.notificationTarget(schedule.getScheduleNo())
					.notificationUrl(url)
					.notificationContent(content)
					.build();
					
			send(notificationDto);
		}
	}
}