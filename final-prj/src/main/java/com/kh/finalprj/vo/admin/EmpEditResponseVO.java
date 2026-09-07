package com.kh.finalprj.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="회원 부서/직급 수정 응답 VO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EmpEditResponseVO {
	
	private boolean result;

}
