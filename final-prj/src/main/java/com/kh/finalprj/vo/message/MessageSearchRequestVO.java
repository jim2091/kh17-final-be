package com.kh.finalprj.vo.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Schema(name = "채널 메세지 검색 요청 데이터")
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class MessageSearchRequestVO {
	@NotBlank
	private String keyword;
	
	@Positive
	private int page;
	
	@Positive
	private int size = 20;
	
	public int getBeginRownum() {
		return page * size - (size - 1);
	}
	
	public int getEndRownum() {
		return page * size;
	}
}
