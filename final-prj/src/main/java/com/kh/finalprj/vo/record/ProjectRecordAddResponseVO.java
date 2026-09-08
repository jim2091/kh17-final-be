package com.kh.finalprj.vo.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "record 등록 응답 정보")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectRecordAddResponseVO {
	private int projectRecordNo;
}
