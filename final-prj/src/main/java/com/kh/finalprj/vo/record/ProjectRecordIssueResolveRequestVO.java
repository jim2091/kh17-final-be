package com.kh.finalprj.vo.record;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(name = "record 이슈 해결 요청 정보")
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectRecordIssueResolveRequestVO {
	@NotBlank
	private String projectRecordIssueResolution;
}
