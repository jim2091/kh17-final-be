package com.kh.finalprj.vo.emp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="회원용 회원목록 응답VO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MemberListVO {
	
	private String empName;
	private String empEmail;
	private String deptName;
	private String positionName;
	private String empContact;
	private String empBirth;
	private String empAddress1;

}
