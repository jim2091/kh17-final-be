package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.ProjectInviteDto;

@Repository
public class ProjectInviteDaoMybatis implements ProjectInviteDao{
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.projectInvite.sequence");
	}

	@Override
	public void add(ProjectInviteDto projectInviteDto) {
		sqlSession.insert("mapper.projectInvite.add",projectInviteDto);
	}

	@Override
	public ProjectInviteDto find(int projectInviteNo) {
		return sqlSession.selectOne("mapper.projectInvite.find",projectInviteNo);
	}

	@Override
	public int countPending(int projectNo, int projectInviteReceiver) {
		Map<String,Object> params = new HashMap<>();
		params.put("projectNo", projectNo);
		params.put("projectInviteReceiver", projectInviteReceiver);
		return sqlSession.selectOne("mapper.projectInvite.countPending",params);
	}

	@Override
	public int accept(int projectInviteNo) {
		return sqlSession.update("mapper.projectInvite.accept",projectInviteNo);
	}

	@Override
	public int reject(int projectInviteNo) {
		return sqlSession.update("mapper.projectInvite.reject",projectInviteNo);
	}

}
