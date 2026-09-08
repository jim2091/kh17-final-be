package com.kh.finalprj.dao;

import com.kh.finalprj.dto.ProjectRecordDto;
import com.kh.finalprj.dto.ProjectRecordIssueDto;

public interface ProjectRecordDao {

	int sequence();
	
	void insert(ProjectRecordDto projectRecordDto);
	
	void insertIssue(ProjectRecordIssueDto projectRecordIssueDto);
	
}
