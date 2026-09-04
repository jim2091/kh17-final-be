package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.vo.admin.AdminComplexSearchRequestVO;
import com.kh.finalprj.vo.admin.AdminComplexSearchResponseVO;
import com.kh.finalprj.vo.admin.AdminInitialSearchRequestVO;
import com.kh.finalprj.vo.admin.AdminInitialSearchResponseVO;
import com.kh.finalprj.vo.emp.EmpListVO;

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
		//+비밀번호 암호화 처리 
		String origin = empDto.getEmpPassword();
		String encrypt = passwordEncoder.encode(origin);
		empDto.setEmpPassword(encrypt);
		return sqlSession.update("mapper.emp.updateAll", empDto)>0;
	}

	@Override
	public List<EmpListVO> selectList() {
		return sqlSession.selectList("mapper.emp.list");
	}

	@Override
	public List<AdminComplexSearchResponseVO> complexSearch(AdminComplexSearchRequestVO vo) {
		return sqlSession.selectList("mapper.emp.complexSearch", vo);
	}

	@Override
	public List<AdminInitialSearchResponseVO> initialSearch(AdminInitialSearchRequestVO vo) {
		return sqlSession.selectList("mapper.emp.initialSearch", vo);
	}

	@Override
	public void connect(int empNo, int attachNo) {

		Map<String, Object> params = new HashMap<>();
		params.put("empNo", empNo);
		params.put("attachNo", attachNo);
		sqlSession.insert("mapper.emp.connect", params);
	}

	@Override
	public Integer findAttachNumber(int empNo) {
		return sqlSession.selectOne("mapper.emp.findAttachNumber", empNo);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
