package com.kh.finalprj.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.DeptDto;

@Repository
public class DeptDaoMybatis implements DeptDao {
	
	@Autowired
	private SqlSession sqlSession;

	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.dept.sequence");
	}

	@Override
	public void insert(DeptDto deptDto) {
		sqlSession.insert("mapper.dept.add", deptDto);		
	}

	@Override
	public DeptDto selectOne(int deptNo) {
		return sqlSession.selectOne("mapper.dept.find", deptNo);
	}
	
	

}
