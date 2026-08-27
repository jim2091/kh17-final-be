package com.kh.finalprj.vo.position;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="직급 목록 조회")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PositionListVO {
	
	private int positionNo;
	private String positionName;
	private String positionInfo;
	private String positionBlock;

}
