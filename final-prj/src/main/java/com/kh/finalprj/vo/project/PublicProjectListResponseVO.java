package com.kh.finalprj.vo.project;

import java.util.List;

import com.kh.finalprj.vo.page.PageVO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "공개 프로젝트 목록VO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PublicProjectListResponseVO {

	private PageVO pageVO;
	private List<ProjectListResponseVO> projectList;
	
}
