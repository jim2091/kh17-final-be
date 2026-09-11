package com.kh.finalprj.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PositionDto {
	
	private int positionNo;
	private String positionName;
	private String positionBlock;
	private String positionInfo;
	private int positionOrder;
	private Timestamp positionCtime;
	private Timestamp positionUtime;
	

}
