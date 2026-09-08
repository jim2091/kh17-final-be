package com.kh.finalprj.vo.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(name = "record 등록 요청 정보")
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectRecordAddRequestVO {
	@NotBlank
	@Pattern(regexp = "DECISION|ISSUE|DELIVERABLE|ETC")
	private String projectRecordType;
	
	@NotBlank
	@Size(max = 300)
	private String projectRecordTitle;
	
	@NotBlank
	private String projectRecordContent;
}
