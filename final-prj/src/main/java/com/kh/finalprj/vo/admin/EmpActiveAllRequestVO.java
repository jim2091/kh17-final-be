package com.kh.finalprj.vo.admin;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name="여러 회원의 상태을 변경하기 위해 요청하는 데이터")
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class EmpActiveAllRequestVO {
	
	private List<Integer> empNos;
	private String empState;

}
