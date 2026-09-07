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
	    private String empEmail;
	    private String empContact;
	    private String empBirth;
	    private String empAddress1;
	    private int count;
	    private int size = 10;
	    
	    
	    //총 페이지수를 계산하여 반환하는 메소드 (pageCount)
		public int getPageCount() {
			if(count == 0) return 0;
			return (count-1) / size + 1;
		}

}
