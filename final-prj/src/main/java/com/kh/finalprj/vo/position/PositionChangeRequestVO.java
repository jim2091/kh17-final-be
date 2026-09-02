package com.kh.finalprj.vo.position;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="직급 수정 요청 정보")
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class PositionChangeRequestVO {
	
	private int PositionNo;
	private String PositionName;
	private String positionInfo;
	private String positionBlock;

}
