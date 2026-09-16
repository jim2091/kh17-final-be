package com.kh.finalprj.service;

import java.util.List;

import com.kh.finalprj.vo.organization.OrganizationDepartmentListResponseVO;
import com.kh.finalprj.vo.organization.OrganizationMemberResponseVO;
import com.kh.finalprj.vo.organization.OrganizationDepartmentDetailResponseVO;

public interface OrganizationService {

	List<OrganizationDepartmentListResponseVO> departmentList();
	
	OrganizationDepartmentDetailResponseVO departmentDetail(
			int deptNo
	);

	List<OrganizationMemberResponseVO> departmentMemberList(
			int deptNo
	);

}