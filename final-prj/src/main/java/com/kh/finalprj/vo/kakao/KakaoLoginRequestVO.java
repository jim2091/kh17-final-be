package com.kh.finalprj.vo.kakao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="카카오 로그인 시 요청 데이터")
@Data
public class KakaoLoginRequestVO {
	
	private Long id;
	private int empNo;

}
