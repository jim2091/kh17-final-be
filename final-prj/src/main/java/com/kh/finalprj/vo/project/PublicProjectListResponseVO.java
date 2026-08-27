package com.kh.finalprj.vo.project;

import java.util.List;

import com.kh.finalprj.vo.page.PageVO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PublicProjectListResponseVO {

	private PageVO pageVO;
	private List<ProjectListResponseVO> projectList;
	
}
