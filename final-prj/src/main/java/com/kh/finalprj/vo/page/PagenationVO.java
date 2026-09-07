package com.kh.finalprj.vo.page;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="페이지네이션 임시 요청 VO")
@Data @JsonIgnoreProperties(ignoreUnknown=true)
public class PagenationVO {
	
	private Integer page=1;
	private Integer size=10;
	
	
	public int getBeginRownum() {
		return page * size - (size-1);
	}
	public int getEndRownum() {
		return page * size;
	}
	
	

}
