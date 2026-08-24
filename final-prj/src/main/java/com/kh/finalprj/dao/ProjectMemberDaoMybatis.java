package com.kh.finalprj.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.ProjectMemberDto;

@Repository
public class ProjectMemberDaoMybatis implements ProjectMemberDao{

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.projectMember.sequence");
	}
	
	@Override
	public void add(ProjectMemberDto projectMemberDto) {
		sqlSession.insert("mapper.projectMember.add",projectMemberDto);
	}

}
