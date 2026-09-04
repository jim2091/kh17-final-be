package com.kh.finalprj.dto;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "기대 결과 DTO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectExpectedResultDto {

	private int projectResultNo;
	private int projectNo;
	private String projectResultContent;
	private String projectResultStatus;
	private int projectResultOrder;
	private Timestamp projectResultCtime;
	private Timestamp projectResultUtime;
}
