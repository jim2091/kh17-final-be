package com.kh.finalprj.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.DeptDto;
import com.kh.finalprj.vo.dept.DeptListVO;

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

	@Override
	public List<DeptListVO> selectList() {
		return sqlSession.selectList("mapper.dept.list");
	}

	@Override
	public boolean updateAll(DeptDto deptDto) {
		return sqlSession.update("mapper.dept.updateAll", deptDto)>0;
	}
	
	

}
