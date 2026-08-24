package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.ScheduleDto;
import com.kh.finalprj.vo.schedule.ScheduleDetailResponseVO;
import com.kh.finalprj.vo.schedule.ScheduleEventVO;

public interface ScheduleDao {
	int sequence();
	void insert(ScheduleDto scheduleDto);
	boolean update(ScheduleDto scheduleDto);
	boolean delete(int scheduleNo);
	ScheduleDetailResponseVO  selectOne(int scheduleNo);
	List<ScheduleEventVO> selectList(int projectNo);
}
