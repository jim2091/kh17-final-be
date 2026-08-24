package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.ScheduleDto;

public interface ScheduleDao {
	int sequence();
	void insert(ScheduleDto scheduleDto);
	boolean update(ScheduleDto scheduleDto);
	boolean delete(int scheduleNo);
	ScheduleDto selectOne(int scheduleNo);
	List<ScheduleDto> selectList(int projectNo);
}
