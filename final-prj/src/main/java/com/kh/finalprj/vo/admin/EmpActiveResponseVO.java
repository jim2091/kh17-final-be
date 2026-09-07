package com.kh.finalprj.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="회원 활성화 수정 응답VO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EmpActiveResponseVO {
	
	private boolean result;

}
