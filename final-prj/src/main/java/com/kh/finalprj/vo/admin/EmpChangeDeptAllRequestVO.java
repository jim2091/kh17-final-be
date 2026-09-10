package com.kh.finalprj.vo.admin;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="회원 부서 일괄 수정을 위한 요청 데이터")
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class EmpChangeDeptAllRequestVO {
	
	private List<Integer> empNos;
	private Integer empDeptNo;

}
