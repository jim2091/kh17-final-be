package com.kh.finalprj.vo.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="사용자 초성검색(관리자용) 요청 데이터")
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class AdminInitialSearchRequestVO {
	
	private String tab;

}
