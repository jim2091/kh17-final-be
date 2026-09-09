package com.kh.finalprj.service;

import java.util.List;

import com.kh.finalprj.vo.record.ProjectRecordAddRequestVO;
import com.kh.finalprj.vo.record.ProjectRecordAddResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordDetailResponseVO;
import com.kh.finalprj.vo.record.ProjectRecordListResponseVO;

public interface ProjectRecordService {
	
	ProjectRecordAddResponseVO add(
		int projectNo,
		int empNo,
		ProjectRecordAddRequestVO requestVO
	);
	
	List<ProjectRecordListResponseVO> list(int projectNo, int empNo);
	
	ProjectRecordDetailResponseVO detail(int projectRecordNo, int empNo);

}
