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
import com.kh.finalprj.vo.record.ProjectRecordListRequestVO;
import com.kh.finalprj.vo.record.ProjectRecordListResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordRelatedResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordSummaryResponseVO;

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
	
	@Override
	public boolean edit(ProjectRecordDto projectRecordDto) {
		return sqlSession.update("mapper.projectRecord.edit", projectRecordDto) > 0;
	}
	
	@Override
	public void deleteTaskList(int projectRecordNo) {
		sqlSession.delete("mapper.projectRecord.deleteTaskList", projectRecordNo);
	}
	
	@Override
	public void deleteNoteList(int projectRecordNo) {
		sqlSession.delete("mapper.projectRecord.deleteNoteList", projectRecordNo);
	}
	
	@Override
	public void deleteAttachList(int projectRecordNo) {
		sqlSession.delete("mapper.projectRecord.deleteAttachList", projectRecordNo);
	}
	
	@Override
	public boolean delete(int projectRecordNo) {
		return sqlSession.delete("mapper.projectRecord.delete", projectRecordNo) > 0;
	}
	
	@Override
	public boolean resolveIssue(ProjectRecordIssueDto projectRecordIssueDto) {
		return sqlSession.update("mapper.projectRecord.resolveIssue", projectRecordIssueDto) > 0;
	}
	
	@Override
	public boolean reopenIssue(int projectRecordNo) {
		return sqlSession.update("mapper.projectRecord.reopenIssue", projectRecordNo) > 0;
	}
	
	@Override
	public boolean updateModifier(ProjectRecordDto projectRecordDto) {
		return sqlSession.update("mapper.projectRecord.updateModifier", projectRecordDto) > 0;
	}
	
	@Override
	public List<ProjectRecordListResponseVO> searchList(int projectNo, ProjectRecordListRequestVO requestVO) {

		Map<String, Object> params = new HashMap<>();
		
		params.put("projectNo", projectNo);
		params.put("request", requestVO);
		
		return sqlSession.selectList("mapper.projectRecord.searchList", params);
	}
	
	@Override
	public List<ProjectRecordRelatedResponseVO> selectRelatedPreviewList(List<Integer> projectRecordNoList) {
		
		return sqlSession.selectList("mapper.projectRecord.selectRelatedPreviewList", projectRecordNoList);
		
	}
	
	@Override
	public ProjectRecordSummaryResponseVO summary(int projectNo) {
		return sqlSession.selectOne("mapper.projectRecord.summary", projectNo);
	}
	
}
