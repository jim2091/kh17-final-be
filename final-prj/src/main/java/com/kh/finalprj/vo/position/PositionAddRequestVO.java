package com.kh.finalprj.vo.position;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="직급 추가 요청 정보")
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class PositionAddRequestVO {
	
	private String positionName;
	private String positionInfo;
	private String positionBlock;

}
