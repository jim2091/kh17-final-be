package com.kh.finalprj.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.EmpDto;

@Repository
public class EmpDaoMybatis implements EmpDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.emp.sequence");
	}

	@Override
	public void insert(EmpDto empDto) {
		String origin = empDto.getEmpPassword();
		String encrypt = passwordEncoder.encode(origin);
		empDto.setEmpPassword(encrypt);
		
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

	@Override
	public EmpDto selectOne(String empEmail) {
		return sqlSession.selectOne("mapper.emp.find2", empEmail);
	}

	@Override
	public boolean updateAll(EmpDto empDto) {
		return sqlSession.update("mapper.emp.updateAll", empDto)>0;
	}

}
