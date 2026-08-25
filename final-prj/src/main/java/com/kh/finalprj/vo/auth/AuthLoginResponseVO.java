package com.kh.finalprj.vo.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="로그인 처리 응답 데이터")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AuthLoginResponseVO {
	
	private int empNo;
	private String empEmail;
	private String empName;
	private String empLevel;

}
