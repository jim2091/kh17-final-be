package com.kh.finalprj.vo.emp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kh.finalprj.vo.page.PagenationVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Schema(name="사용자 초성검색(사용자용) 요청 데이터")
@Data @JsonIgnoreProperties(ignoreUnknown=true)
public class MemberInitialSearchRequestVO {
	
	private String tab;
	private PagenationVO pageVO;

}
