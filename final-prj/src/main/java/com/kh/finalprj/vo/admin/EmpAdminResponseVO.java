package com.kh.finalprj.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="관리자/멤버 레벨 변경 응답 VO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EmpAdminResponseVO {
	
	private boolean result;

}
