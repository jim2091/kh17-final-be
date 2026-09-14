package com.kh.finalprj.vo.record;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "프로젝트 record 목록 검색 응답 데이터")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProjectRecordSearchResponseVO {
	private List<ProjectRecordListResponseVO> recordList;
	private boolean last;
}
