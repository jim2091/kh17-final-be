package com.kh.finalprj.vo.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Schema(name = "채널 기존 메시지 요청 데이터")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelMessageRequestVO {
	@Positive
	public int size = 100;
	@Positive
	public Integer lastMessageNo;
}