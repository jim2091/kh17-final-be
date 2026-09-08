package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.EmpDto;
import com.kh.finalprj.vo.admin.AdminComplexSearchRequestVO;
import com.kh.finalprj.vo.admin.AdminComplexSearchResponseVO;
import com.kh.finalprj.vo.admin.AdminInitialSearchRequestVO;
import com.kh.finalprj.vo.admin.AdminInitialSearchResponseVO;
import com.kh.finalprj.vo.admin.EmpEditRequestVO;
import com.kh.finalprj.vo.emp.EmpListVO;
import com.kh.finalprj.vo.page.PageVO;


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
	
	Integer findAttachNumber(int empNo);
	

	//presence 변경
	void updatePresence(int empNo, String empPresence);

	boolean activeRequest(int empNo);
	boolean memberEdit(EmpEditRequestVO vo);

	
	//페이지네이션
	int count();
	List<EmpListVO> selectList(int beginRownum, int endRownum, String sort);
	
	//검색 결과 수
	int searchCount(String keyword);
	//초성 검색 결과 수
	int tabCount(String tab);
	
	
	
	
	
	
	
	
	
	
	
	
	
}
