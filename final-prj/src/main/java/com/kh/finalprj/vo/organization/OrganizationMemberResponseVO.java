package com.kh.finalprj.vo.organization;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "조직 부서 구성원 응답")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationMemberResponseVO {

	private int empNo;

	private String empName;
	private String empEmail;
	private String empContact;

	private String positionName;

	private String empPresence;
	private String empLevel;

	private Integer attachNo;

}