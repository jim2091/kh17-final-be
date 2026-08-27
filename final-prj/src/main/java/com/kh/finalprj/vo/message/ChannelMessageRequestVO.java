package com.kh.finalprj.vo.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Schema(name = "특정 채널의 과거 메세지를 조회하기 위한 요청")
@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelMessageRequestVO {
	@Positive
	public int size = 100;
	@Positive
	public Integer lastMessageNo;
}