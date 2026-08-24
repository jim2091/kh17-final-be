package com.kh.finalprj.vo.schedule;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(name = "일정 수정 요청 정보")
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleEditRequestVO {

	@NotBlank
	@Size(max = 300)
	private String scheduleTitle;
	@Size(max = 1000)
	private String scheduleContent;
	@NotNull
	private Timestamp scheduleStart;
	private Timestamp scheduleEnd;
	@Size(max = 300)
	private String schedulePlace;
}
