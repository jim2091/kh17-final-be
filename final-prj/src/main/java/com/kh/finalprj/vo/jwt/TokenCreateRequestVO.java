package com.kh.finalprj.vo.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TokenCreateRequestVO {
	
	private int empNo;
	private String empLevel;
	

}
