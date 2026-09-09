package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.dto.ProjectRecordDto;
import com.kh.finalprj.dto.ProjectRecordIssueDto;
import com.kh.finalprj.vo.record.ProjectRecordDetailResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordListResponseVO;

public interface ProjectRecordDao {

	int sequence();
	
	void insert(ProjectRecordDto projectRecordDto);
	
	void insertIssue(ProjectRecordIssueDto projectRecordIssueDto);
	
	List<ProjectRecordListResponseVO> list(int projectNo);
	
	ProjectRecordDetailResponseVO detail(int projectRecordNo);
}
