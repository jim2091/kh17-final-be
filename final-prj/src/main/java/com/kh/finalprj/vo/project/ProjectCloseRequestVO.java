package com.kh.finalprj.vo.project;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "프로젝트 종료 요청VO")
@Data
public class ProjectCloseRequestVO {
	
	private String closeSummary;
	private String closeGood;
	private String closeBad;
	private String closeImprovement;
	//예상 결과 평가 목록
	private List<ProjectResultCloseRequestVO> resultList;
}
