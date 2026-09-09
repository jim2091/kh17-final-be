package com.kh.finalprj.vo.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "record 연관 원본 응답 데이터")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectRecordRelatedResponseVO {
	private String relatedType;
	private int relatedNo;
	private String relatedTitle;
	private String relatedStatus;
}
