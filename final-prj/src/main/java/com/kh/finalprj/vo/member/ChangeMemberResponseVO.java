package com.kh.finalprj.vo.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="본인 정보 변경 완료 VO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ChangeMemberResponseVO {

	private boolean status;
	private String message;
}
