package com.kh.finalprj.vo.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="키워드로 사용자 목록을 검색하기 요청 VO")
@Data @JsonIgnoreProperties(ignoreUnknown=true)
public class EmpSearchRequestVO {
	
	private String keyword;

}
