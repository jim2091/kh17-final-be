package com.kh.finalprj.vo.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="내 정보 조회 데이터")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MemberMeResponseVO {
	

	private String empName;
	private String empEmail;
	private String deptName;
	private String positionName;
	private String empBirth;
	private String empContact;
	private String empPost;
	private String empAddress1;
	private String empAddress2;
	
}
