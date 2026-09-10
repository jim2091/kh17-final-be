package com.kh.finalprj.vo.record;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(name = "record 수정 요청 정보")
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectRecordEditRequestVO {
	@NotBlank
	@Size(max = 300)
	private String projectRecordTitle;
	
	@NotBlank
	private String projectRecordContent;
	
	private List<Integer> taskNoList;
	private List<Integer> noteNoList;
	private List<Integer> attachNoList;
	
}
