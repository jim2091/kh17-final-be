package com.kh.finalprj.service;

import com.kh.finalprj.vo.ProjectCreateRequestVO;

public interface ProjectService{

	int create(ProjectCreateRequestVO requestVO, int empNo);
}
