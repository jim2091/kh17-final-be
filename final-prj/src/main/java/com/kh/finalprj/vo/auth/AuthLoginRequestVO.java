package com.kh.finalprj.vo.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="로그인 처리 요청 데이터")
@Data
public class AuthLoginRequestVO {
	private String empEmail;
	private String empPassword;

}
