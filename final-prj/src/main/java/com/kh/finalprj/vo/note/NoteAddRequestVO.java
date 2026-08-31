package com.kh.finalprj.vo.note;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(name = "노트 등록 요청 VO")
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class NoteAddRequestVO {
	@NotBlank
	@Size(max = 300)
	private String noteTitle;
	
	@NotBlank
	private String noteContent;
}
