package com.kh.finalprj.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.ProjectCloseDto;
@Repository
public class ProjectCloseDaoMybatis implements ProjectCloseDao{

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public void add(ProjectCloseDto projectCloseDto) {
		sqlSession.insert("mapper.projectClose.add",projectCloseDto);
	}

}
