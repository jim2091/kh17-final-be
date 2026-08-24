package com.kh.finalprj.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.EmpRefreshDto;

@Repository
public class EmpRefreshDaoMybatis implements EmpRefreshDao {
	
	@Autowired
	private SqlSession sqlSession;

	@Override
	public void insertOrUpdate(EmpRefreshDto empRefreshDto) {

		EmpRefreshDto findDto = sqlSession.selectOne("mapper.empRefresh.find", empRefreshDto);
		if(findDto == null) {
			sqlSession.insert("mapper.empRefresh.add", empRefreshDto);
		}
		else {
			sqlSession.update("mapper.empRefresh.change", empRefreshDto);
		}
		
	}

	@Override
	public void delete(EmpRefreshDto empRefreshDto) {

		sqlSession.delete("mapper.empRefresh.delete", empRefreshDto);
	}

	@Override
	public EmpRefreshDto find(EmpRefreshDto empRefreshDto) {
		return sqlSession.selectOne("mapper.empRefresh.find", empRefreshDto);
	}

}
