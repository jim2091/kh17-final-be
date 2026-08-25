package com.kh.finalprj.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.PositionDto;

@Repository
public class PositionDaoMybatis implements PositionDao {
	
	@Autowired
	private SqlSession sqlSession;

	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.position.sequence");
	}

	@Override
	public void insert(PositionDto positionDto) {
		sqlSession.insert("mapper.position.add", positionDto);				
	}

	@Override
	public PositionDto selectOne(int positionNo) {
		return sqlSession.selectOne("mapper.position.find", positionNo);
	}
	
	

}
