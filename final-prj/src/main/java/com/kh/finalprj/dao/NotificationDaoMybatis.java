package com.kh.finalprj.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.NotificationDto;

@Repository
public class NotificationDaoMybatis implements NotificationDao {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.notification.sequence");
	}

	@Override
	public void insert(NotificationDto notificationDto) {
		sqlSession.insert("mapper.notification.insert", notificationDto);
	}

	@Override
	public List<NotificationDto> selectListByReceiver(int empNo) {
		return sqlSession.selectList("mapper.notification.selectListByReceiver", empNo);
	}

	@Override
	public int countUnRead(int empNo) {
		return sqlSession.selectOne("mapper.notification.countUnRead", empNo);
	}

	@Override
	public boolean markAsRead(int notificationNo, int empNo) {
		return sqlSession.update("mapper.notification.markAsRead",
				Map.of("notificationNo", notificationNo, "empNo", empNo)) > 0;
	}

	@Override
	public boolean markAllAsRead(int empNo) {
		return sqlSession.update("mapper.notification.markAllAsRead", empNo) > 0;
	}

	@Override
	public boolean delete(int notificationNo, int empNo) {
		return sqlSession.delete("mapper.notification.delete",
				Map.of("notificationNo", notificationNo, "empNo", empNo)) > 0;
	}

}
