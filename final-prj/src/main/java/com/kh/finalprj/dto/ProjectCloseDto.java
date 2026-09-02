package com.kh.finalprj.dto;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Schema(name = "프로젝트 종료DTO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectCloseDto {

	private int projectNo;
	
	private String closeSummary;
	private String closeGood;
	private String closeBad;
	private String closeImprovement;
	
	private Timestamp closeCtime;
	private Timestamp closeUtime;
}
