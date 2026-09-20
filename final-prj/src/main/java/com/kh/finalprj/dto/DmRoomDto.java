package com.kh.finalprj.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DmRoomDto {

	private int dmRoomNo;
	private int dmRoomEmp1No;
	private int dmRoomEmp2No;
	private Timestamp dmRoomCtime;

}