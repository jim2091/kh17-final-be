package com.kh.finalprj.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="회원가입초대 완료")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class JoinResponseVO {
	
	private boolean result;

}
