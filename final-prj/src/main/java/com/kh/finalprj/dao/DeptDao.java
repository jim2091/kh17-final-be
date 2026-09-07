package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.DeptDto;
import com.kh.finalprj.vo.dept.DeptListSearchVO;
import com.kh.finalprj.vo.dept.DeptListVO;

public interface DeptDao {
	
	int sequence();
	void insert(DeptDto deptDto);
	
	DeptDto selectOne(int deptNo);
	
	List<DeptListVO> selectList();
	
	List<DeptListSearchVO> listSearch();
	
	boolean updateAll(DeptDto deptDto);

}
