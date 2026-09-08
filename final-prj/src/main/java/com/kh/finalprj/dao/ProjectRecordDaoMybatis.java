package com.kh.finalprj.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.ProjectRecordDto;
import com.kh.finalprj.dto.ProjectRecordIssueDto;

@Repository
public class ProjectRecordDaoMybatis implements ProjectRecordDao{
	
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public int sequence() {
		return sqlSession.selectOne("mapper.projectRecord.sequence");
	}
	
	@Override
	public void insert(ProjectRecordDto projectRecordDto) {
		sqlSession.insert("mapper.projectRecord.insert", projectRecordDto);
	}
	
	@Override
	public void insertIssue(ProjectRecordIssueDto projectRecordIssueDto) {
		sqlSession.insert("mapper.projectRecord.insertIssue", projectRecordIssueDto);
	}
}
