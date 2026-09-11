package com.kh.finalprj.service;

import java.util.List;

import com.kh.finalprj.vo.record.ProjectRecordAddRequestVO;
import com.kh.finalprj.vo.record.ProjectRecordAddResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordDetailResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordEditRequestVO;
import com.kh.finalprj.vo.record.ProjectRecordIssueResolveRequestVO;
import com.kh.finalprj.vo.record.ProjectRecordListResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordRelatedAddRequestVO;

public interface ProjectRecordService {
	
	ProjectRecordAddResponseVO add(
		int projectNo,
		int empNo,
		ProjectRecordAddRequestVO requestVO
	);
	
	List<ProjectRecordListResponseVO> list(int projectNo, int empNo);
	
	ProjectRecordDetailResponseVO detail(int projectRecordNo, int empNo);
	
	void edit(int projectRecordNo, int empNo, ProjectRecordEditRequestVO request);
	
	void delete(int projectRecordNo, int empNo);
	
	void resolveIssue(int projectRecordNo, int empNo, ProjectRecordIssueResolveRequestVO request);
	
	void reopenIssue(int projectRecordNo, int empNo);
	
	//기존 record에 원본 연결
	void addRelated(int projectRecordNo, int empNo, ProjectRecordRelatedAddRequestVO request);

}
