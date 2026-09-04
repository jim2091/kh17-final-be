package com.kh.finalprj.vo.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Schema(name = "프로젝트 기대결과 요청VO")
@Data
public class ProjectExpectedResultRequestVO {

	private String projectResultContent;
}
