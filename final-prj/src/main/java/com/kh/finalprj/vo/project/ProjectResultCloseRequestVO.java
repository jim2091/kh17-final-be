package com.kh.finalprj.vo.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "예상 결과 평가용VO")
@Data
public class ProjectResultCloseRequestVO {

	private int projectResultNo;
	private String projectResultStatus;
}
