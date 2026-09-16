package com.kh.finalprj.dao;

import java.util.List;

import com.kh.finalprj.vo.organization.OrganizationDepartmentDetailResponseVO;
import com.kh.finalprj.vo.organization.OrganizationDepartmentListResponseVO;
import com.kh.finalprj.vo.organization.OrganizationMemberResponseVO;

public interface OrganizationDao {

	//일반 사용자용 부서 목록 조회
	List<OrganizationDepartmentListResponseVO> departmentList();
	
	//부서 상세 조회
	OrganizationDepartmentDetailResponseVO departmentDetail(
			int deptNo
	);

	//부서 구성원 조회
	List<OrganizationMemberResponseVO> departmentMemberList(
			int deptNo
	);

}