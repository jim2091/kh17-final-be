package com.kh.finalprj.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.dto.ProjectRecordDto;
import com.kh.finalprj.dto.ProjectRecordIssueDto;
import com.kh.finalprj.vo.record.ProjectRecordDetailResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordListResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordRelatedResponseVO;

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
	
	@Override
	public List<ProjectRecordListResponseVO> list(int projectNo) {
		return sqlSession.selectList("mapper.projectRecord.list", projectNo);
	}
	
	@Override
	public ProjectRecordDetailResponseVO detail(int projectRecordNo) {
		return sqlSession.selectOne("mapper.projectRecord.detail", projectRecordNo);
	}
	
	@Override
	public void insertTask(int projectRecordNo, int taskNo) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("projectRecordNo", projectRecordNo);
		params.put("taskNo", taskNo);
		
		sqlSession.insert("mapper.projectRecord.insertTask", params);
	}
	
	@Override
	public void insertMessage(int projectRecordNo, int chatMessageNo) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("projectRecordNo", projectRecordNo);
		params.put("chatMessageNo", chatMessageNo);
		
		sqlSession.insert("mapper.projectRecord.insertMessage", params);
	}
	
	@Override
	public void insertNote(int projectRecordNo, int noteNo) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("projectRecordNo", projectRecordNo);
		params.put("noteNo", noteNo);
		
		sqlSession.insert("mapper.projectRecord.insertNote", params);
	}
	
	@Override
	public void insertAttach(int projectRecordNo, int attachNo) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("projectRecordNo", projectRecordNo);
		params.put("attachNo", attachNo);
		
		sqlSession.insert("mapper.projectRecord.insertAttach", params);
	}
	
	@Override
	public List<ProjectRecordRelatedResponseVO> selectRelatedList(int projectRecordNo) {
		return sqlSession.selectList("mapper.projectRecord.selectRelatedList", projectRecordNo);
	}
}
