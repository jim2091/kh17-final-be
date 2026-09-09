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
import com.kh.finalprj.vo.admin.EmpEditRequestVO;
import com.kh.finalprj.vo.admin.EmpSearchRequestVO;
import com.kh.finalprj.vo.admin.EmpSearchResponseVO;
import com.kh.finalprj.vo.emp.EmpListVO;
import com.kh.finalprj.vo.page.PagenationVO;

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
		if(empDto.getEmpPassword() != null && !empDto.getEmpPassword().isBlank()) {
			
			String origin = empDto.getEmpPassword();
			String encrypt = passwordEncoder.encode(origin);
			empDto.setEmpPassword(encrypt);
		}
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

	@Override
	public void updatePresence(int empNo, String empPresence) {

		Map<String, Object> params = new HashMap<>();
		params.put("empNo", empNo);
		params.put("empPresence", empPresence);
		
		sqlSession.update("mapper.emp.updatePresence", params);
		
	}

	public boolean activeRequest(int empNo) {
		return sqlSession.update("mapper.emp.active", empNo)>0;
	}

	@Override
	public boolean memberEdit(EmpEditRequestVO vo) {
		return sqlSession.update("mapper.emp.memberEdit", vo)>0;
	}

	
	@Override
	public List<EmpListVO> selectList(PagenationVO pageVO) {
		
		return sqlSession.selectList("mapper.emp.listPage", pageVO);
	}

	@Override
	public int count() {
		return sqlSession.selectOne("mapper.emp.count");
	}

	@Override
	public int searchCount(String keyword) {
		return sqlSession.selectOne("mapper.emp.searchCount", keyword);
	}

	@Override
	public int tabCount(String tab) {
		return sqlSession.selectOne("mapper.emp.tabCount", tab);
	}

	@Override
	public List<EmpSearchResponseVO> empSearch(EmpSearchRequestVO vo) {
		return sqlSession.selectList("mapper.emp.empSearch", vo);
	}

	@Override
	public boolean becomeAdmin(int empNo) {
		return sqlSession.update("mapper.emp.becomeAdmin", empNo)>0;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
