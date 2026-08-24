package com.kh.finalprj.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.EmpDto;

@Repository
public class EmpDaoMybatis implements EmpDao {
	
	@Autowired
	private SqlSession sqlSession;

	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.emp.sequence");
	}

	@Override
	public void insert(EmpDto empDto) {
		sqlSession.insert("mapper.emp.add", empDto);

	}

	@Override
	public EmpDto selectOne(int empNo) {
		return sqlSession.selectOne("mapper.emp.find", empNo);
	}

	@Override
	public boolean checkAvailableEmail(String empEmail) {
		int count = sqlSession.selectOne("mapper.emp.countEmpEmail", empEmail);
		return count == 0;
	}

}
