package com.kh.finalprj.vo.emp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="회원 목록 리스트")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EmpListVO {
		
		private int empNo;
	    private String empName;
	    private String deptName;
	    private String positionName;
	    private String empState;

}
