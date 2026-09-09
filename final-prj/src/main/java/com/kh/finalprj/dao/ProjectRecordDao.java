package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.ProjectRecordDto;
import com.kh.finalprj.dto.ProjectRecordIssueDto;
import com.kh.finalprj.vo.record.ProjectRecordDetailResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordListResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordRelatedResponseVO;

public interface ProjectRecordDao {

	int sequence();
	
	void insert(ProjectRecordDto projectRecordDto);
	
	void insertIssue(ProjectRecordIssueDto projectRecordIssueDto);
	
	List<ProjectRecordListResponseVO> list(int projectNo);
	
	ProjectRecordDetailResponseVO detail(int projectRecordNo);
	
	void insertTask(int projectRecordNo, int taskNo);
	void insertMessage(int projectRecordNo, int chatMessageNo);
	void insertNote(int projectRecordNo, int noteNo);
	void insertAttach(int projectRecordNo, int attachNo);
	
	List<ProjectRecordRelatedResponseVO> selectRelatedList(int projectRecordNo);
}
