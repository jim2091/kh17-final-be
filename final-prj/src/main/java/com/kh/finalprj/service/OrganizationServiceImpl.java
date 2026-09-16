package com.kh.finalprj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.finalprj.dao.OrganizationDao;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.vo.organization.OrganizationDepartmentDetailResponseVO;
import com.kh.finalprj.vo.organization.OrganizationDepartmentListResponseVO;
import com.kh.finalprj.vo.organization.OrganizationMemberResponseVO;

@Service
public class OrganizationServiceImpl implements OrganizationService {

	@Autowired
	private OrganizationDao organizationDao;


	@Override
	public List<OrganizationDepartmentListResponseVO> departmentList() {

		return organizationDao.departmentList();

	}
	
	@Override
	public OrganizationDepartmentDetailResponseVO departmentDetail(
			int deptNo) {

		OrganizationDepartmentDetailResponseVO result =
				organizationDao.departmentDetail(deptNo);

		if(result == null) {
			throw new TargetNotfoundException(
					"존재하지 않는 부서입니다."
			);
		}

		return result;

	}


	@Override
	public List<OrganizationMemberResponseVO> departmentMemberList(
			int deptNo) {

		//존재하는 부서인지 확인
		OrganizationDepartmentDetailResponseVO department =
				organizationDao.departmentDetail(deptNo);

		if(department == null) {
			throw new TargetNotfoundException(
					"존재하지 않는 부서입니다."
			);
		}

		return organizationDao.departmentMemberList(
				deptNo
		);

	}

}