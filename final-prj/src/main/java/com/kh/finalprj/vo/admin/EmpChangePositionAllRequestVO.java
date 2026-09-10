package com.kh.finalprj.vo.admin;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="회원 직급 일괄 변경하기 위한 데이터")
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class EmpChangePositionAllRequestVO {
	
	private List<Integer> empNos;
	private Integer empPositionNo;

}
