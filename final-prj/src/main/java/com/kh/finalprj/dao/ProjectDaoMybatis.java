package com.kh.finalprj.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.ProjectDto;
@Repository
public class ProjectDaoMybatis implements ProjectDao{
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.project.sequence");
	}

	@Override
	public void add(ProjectDto projectDto) {
		sqlSession.insert("mapper.project.add",projectDto);
	}

}
