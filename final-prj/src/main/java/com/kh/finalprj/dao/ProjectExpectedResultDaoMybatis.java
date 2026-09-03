package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectExpectedResultDaoMybatis implements ProjectExpectedResultDao {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public int updateStatus(int projectNo, int projectResultNo, String projectResultStatus) {
		Map<String, Object> params = new HashMap<>();
		params.put("projectNo", projectNo);
		params.put("projectResultNo", projectResultNo);
		params.put("projectResultStatus", projectResultStatus);

		return sqlSession.update("mapper.projectExpectedResult.updateStatus", params);
	}

}
