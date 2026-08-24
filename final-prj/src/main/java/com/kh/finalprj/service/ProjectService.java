package com.kh.finalprj.service;

import com.kh.finalprj.vo.project.ProjectCreateRequestVO;

public interface ProjectService{

	int create(ProjectCreateRequestVO requestVO, int empNo);
}
