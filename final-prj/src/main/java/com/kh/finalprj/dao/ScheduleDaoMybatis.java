package com.kh.finalprj.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.ScheduleDto;
import com.kh.finalprj.vo.schedule.ScheduleDetailResponseVO;
import com.kh.finalprj.vo.schedule.ScheduleEventVO;

@Repository
public class ScheduleDaoMybatis implements ScheduleDao{
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.schedule.sequence");
	}
	
	@Override
	public void insert(ScheduleDto scheduleDto) {
		sqlSession.insert("mapper.schedule.add", scheduleDto);
	}
	
	@Override
	public boolean update(ScheduleDto scheduleDto) {
		return sqlSession.update("mapper.schedule.update", scheduleDto) > 0;
	}
	
	@Override
	public boolean delete(int scheduleNo) {
		return sqlSession.delete("mapper.schedule.delete", scheduleNo) > 0;
	}
	
	@Override
	public ScheduleDetailResponseVO  selectOne(int scheduleNo) {
		return sqlSession.selectOne("mapper.schedule.find", scheduleNo);
	}
	
	@Override
	public List<ScheduleEventVO> selectList(int projectNo) {
	    return sqlSession.selectList("mapper.schedule.list", projectNo);
	}
}
