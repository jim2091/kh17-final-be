package com.kh.finalprj.service;

import com.kh.finalprj.vo.record.ProjectRecordAddRequestVO;
import com.kh.finalprj.vo.record.ProjectRecordAddResponseVO;

public interface ProjectRecordService {
	
	ProjectRecordAddResponseVO add(
		int projectNo,
		int empNo,
		ProjectRecordAddRequestVO requestVO
	);

}
