package com.kh.finalprj.vo.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Schema(name = "기존 record에 원본 추가 요청 정보")
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectRecordRelatedAddRequestVO {
	@NotBlank
	@Pattern(regexp = "^(TASK|MESSAGE|NOTE|ATTACH)$")
	private String relatedType;
	
	@NotNull
	private int relateNo;
}
