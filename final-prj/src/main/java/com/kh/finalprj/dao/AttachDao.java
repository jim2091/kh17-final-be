package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.AttachDto;

public interface AttachDao {
	int sequence();
	void insert(AttachDto attachDto);
	
	AttachDto selectOne(int attachNo);
	AttachDto selectOne(Integer attachNo);
	
	boolean delete(int attachNo);
	List<AttachDto> selectList(List<Integer> attachNumbers);
	
	List<AttachDto> selectListByProject(int projectNo);
	List<AttachDto> selectListByProjectAndKeyword(int projectNo, String keyword);
}
