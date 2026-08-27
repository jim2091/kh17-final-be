package com.kh.finalprj.vo.position;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="사용자 추가 응답 정보")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PositionAddResponseVO {
	
	private int positionNo;
	private String positionName;
	private String positionInfo;
	private String positionBlock;

}
