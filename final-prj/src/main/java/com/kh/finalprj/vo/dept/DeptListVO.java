package com.kh.finalprj.vo.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="부서 목록 리스트")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DeptListVO {
	
	private int deptNo;
	private String deptName;
	private String deptInfo;
	private String deptBlock;

}
