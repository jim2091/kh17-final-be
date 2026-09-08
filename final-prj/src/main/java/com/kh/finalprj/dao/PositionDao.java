package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.PositionDto;
import com.kh.finalprj.vo.dept.DeptListVO;
import com.kh.finalprj.vo.page.PagenationVO;
import com.kh.finalprj.vo.position.PositionListSearchVO;
import com.kh.finalprj.vo.position.PositionListVO;

public interface PositionDao {
	
	int sequence();
	void insert(PositionDto positionDto);
	
	PositionDto selectOne(int positionNo);
	
	List<PositionListVO> selectList();
	
	boolean updateAll(PositionDto positionDto);
	
	List<PositionListSearchVO> listSearch();
	
	//페이지네이션
	int count();
	List<PositionListVO> selectList(PagenationVO pageVO);

}
