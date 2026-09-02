package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.vo.admin.AdminComplexSearchRequestVO;
import com.kh.finalprj.vo.admin.AdminComplexSearchResponseVO;
import com.kh.finalprj.vo.admin.AdminInitialSearchRequestVO;
import com.kh.finalprj.vo.admin.AdminInitialSearchResponseVO;
import com.kh.finalprj.vo.emp.EmpListVO;


public interface EmpDao {
	
	int sequence();
	void insert(EmpDto empDto);
	
	EmpDto selectOne(int empNo);
	boolean checkAvailableEmail(String empEmail);
	
	EmpDto selectOne(String empEmail);
	
	boolean updateAll(EmpDto empDto);
	
	
	
	List<EmpListVO> selectList();
	
	List<AdminComplexSearchResponseVO> complexSearch(AdminComplexSearchRequestVO vo);

	List<AdminInitialSearchResponseVO> initialSearch(AdminInitialSearchRequestVO vo);
	
	
	
	void connect(int empNo, int attachNo);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
