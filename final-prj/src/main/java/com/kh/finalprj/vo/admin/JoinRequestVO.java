package com.kh.finalprj.vo.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="회원가입 초대 요청 정보")
@Data 
@JsonIgnoreProperties(ignoreUnknown = true)
public class JoinRequestVO {
	
	private String empEmail;
	
	
	

}
