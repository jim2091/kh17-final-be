package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.service.OrganizationService;
import com.kh.finalprj.vo.organization.OrganizationDepartmentDetailResponseVO;
import com.kh.finalprj.vo.organization.OrganizationDepartmentListResponseVO;
import com.kh.finalprj.vo.organization.OrganizationMemberResponseVO;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "조직 정보 조회 서비스")
@CommonsApiResponse
@RestController
@RequestMapping("/api/organization")
public class OrganizationRestController {

	@Autowired
	private OrganizationService organizationService;


	//부서 목록 조회
	@GetMapping("/departments")
	public List<OrganizationDepartmentListResponseVO> departmentList() {

		return organizationService.departmentList();

	}
	
	//부서 상세 조회
	@GetMapping("/departments/{deptNo}")
	public OrganizationDepartmentDetailResponseVO departmentDetail(
			@PathVariable int deptNo) {

		return organizationService.departmentDetail(
				deptNo
		);

	}


	//부서 구성원 조회
	@GetMapping("/departments/{deptNo}/members")
	public List<OrganizationMemberResponseVO> departmentMemberList(
			@PathVariable int deptNo) {

		return organizationService.departmentMemberList(
				deptNo
		);

	}

}