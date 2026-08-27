package com.kh.finalprj.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.PositionDto;
import com.kh.finalprj.vo.position.PositionListVO;

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

	@Override
	public List<PositionListVO> selectList() {
		return sqlSession.selectList("mapper.position.list");
	}
	
	

}
