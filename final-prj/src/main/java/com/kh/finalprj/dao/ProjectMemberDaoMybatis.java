package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.ProjectMemberDto;
import com.kh.finalprj.vo.project.ProjectMemberListResponseVO;

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

	@Override
	public Integer findProjectMemberNo(int projectNo, int empNo) {
		Map<String, Object> param = new HashMap<>();
		param.put("projectNo", projectNo);
		param.put("empNo", empNo);
		return sqlSession.selectOne(
				"mapper.projectMember.findProjectMemberNo", param);
	}

	@Override
	public ProjectMemberDto findMember(int projectNo, int empNo) {
		Map<String, Object> param = new HashMap<>();
		param.put("projectNo", projectNo);
		param.put("empNo", empNo);
		return sqlSession.selectOne(
				"mapper.projectMember.findMember", param);
	}

	@Override
	public ProjectMemberDto findMember(int projectMemberNo) {
		return sqlSession.selectOne("mapper.projectMember.findMember2", projectMemberNo);
	}

	public String selectRole(int projectNo, int empNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("projectNo", projectNo);
		params.put("empNo", empNo);
		return sqlSession.selectOne("mapper.projectMember.selectRole",params);
	}

	@Override
	public List<ProjectMemberListResponseVO> selectProjectMemberList(int projectNo) {
		return  sqlSession.selectList("mapper.projectMember.selectProjectMemberList",projectNo);
	}

	@Override
	public void updateRole(int projectMemberNo, String projectMemberRole) {
		Map<String,Object>params = new HashMap<>();
		params.put("projectMemberNo",projectMemberNo);
		params.put("projectMemberRole", projectMemberRole);
		sqlSession.update("mapper.projectMember.updateRole",params);
	}
}
