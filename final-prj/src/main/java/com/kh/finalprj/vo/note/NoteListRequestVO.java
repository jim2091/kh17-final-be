package com.kh.finalprj.vo.note;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "노트 목록 조회 요청 VO")
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class NoteListRequestVO {
	private int projectNo;
	private Integer lastNo;
	private int size = 10;
	
	//검색 관련
	private String type;
	private String keyword;
}
