package com.kh.finalprj.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.finalprj.vo.organization.OrganizationDepartmentDetailResponseVO;
import com.kh.finalprj.vo.organization.OrganizationDepartmentListResponseVO;
import com.kh.finalprj.vo.organization.OrganizationMemberResponseVO;

@Repository
public class OrganizationDaoMybatis implements OrganizationDao {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<OrganizationDepartmentListResponseVO> departmentList() {

		return sqlSession.selectList(
				"mapper.organization.departmentList"
		);

	}
	
	@Override
	public OrganizationDepartmentDetailResponseVO departmentDetail(
			int deptNo) {

		return sqlSession.selectOne(
				"mapper.organization.departmentDetail",
				deptNo
		);

	}


	@Override
	public List<OrganizationMemberResponseVO> departmentMemberList(
			int deptNo) {

		return sqlSession.selectList(
				"mapper.organization.departmentMemberList",
				deptNo
		);

	}

}