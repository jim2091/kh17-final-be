package com.kh.finalprj.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="회원 토큰 갱신 DTO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EmpRefreshDto {
	
	private int empNo;
	private String userAgent;
	private String userAddress;
	private String tokenValue;
	
	private String sessionId;
}
