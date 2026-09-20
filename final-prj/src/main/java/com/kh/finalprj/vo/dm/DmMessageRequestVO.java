package com.kh.finalprj.vo.dm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Positive;
import lombok.Data;


@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class DmMessageRequestVO {

	@Positive
	private int size = 50;

	@Positive
	private Integer lastMessageNo;

}