package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.ProjectExpectedResultDto;
@Repository
public class ProjectExpectedResultDaoMybatis implements ProjectExpectedResultDao{
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public int updateStatus(int projectNo, int projectResultNo, String projectResultStatus) {
		Map<String,Object> params = new HashMap<>();
		params.put("projectNo", projectNo);
		params.put("projectResultNo", projectResultNo);
		params.put("projectResultStatus",projectResultStatus);
		
		return sqlSession.update("mapper.projectExpectedResult.updateStatus",params);
	}

	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.projectExpectedResult.sequence");
	}
	
	@Override
	public int nextOrder(int projectNo) {
		return sqlSession.selectOne("mapper.projectExpectedResult.nextOrder",projectNo);
	}

	@Override
	public void add(ProjectExpectedResultDto projectExpectedResultDto) {
		sqlSession.insert("mapper.projectExpectedResult.add",projectExpectedResultDto);
	}

	@Override
	public List<ProjectExpectedResultDto> selectList(int projectNo) {
		return sqlSession.selectList("mapper.projectExpectedResult.selectList",projectNo);
	}

	@Override
	public ProjectExpectedResultDto find(int projectNo, int projectResultNo) {
		Map<String,Object> params = new HashMap<>();
		params.put("projectNo", projectNo);
		params.put("projectResultNo", projectResultNo);
		
		return sqlSession.selectOne("mapper.projectExpectedResult.find",params);
	}

	@Override
	public boolean update(int projectNo, int projectResultNo, String projectResultContent) {
		Map<String,Object>params = new HashMap<>();
		params.put("projectNo", projectNo);
		params.put("projectResultNo", projectResultNo);
		params.put("projectResultContent", projectResultContent);
		
		return sqlSession.update("mapper.projectExpectedResult.update",params)>0;
	}

	@Override
	public boolean delete(int projectNo, int projectResultNo) {
		Map<String,Object>params = new HashMap<>();
		params.put("projectNo", projectNo);
		params.put("projectResultNo", projectResultNo);	
		
		return sqlSession.delete("mapper.projectExpectedResult.delete",params)>0;
	}


}
