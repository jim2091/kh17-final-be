package com.kh.finalprj.vo.schedule;

import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(name = "일정 등록 요청 정보")
@Data
public class ScheduleAddRequestVO {
	
	@NotNull
	private int projectNo;
	//작성자 번호는 로그인 정보로
	
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
