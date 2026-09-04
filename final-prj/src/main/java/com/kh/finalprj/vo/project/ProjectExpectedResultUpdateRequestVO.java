package com.kh.finalprj.vo.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "기대결과 수정 요청VO")
@Data @NoArgsConstructor @AllArgsConstructor
public class ProjectExpectedResultUpdateRequestVO {

	private String projectResultContent;
}
