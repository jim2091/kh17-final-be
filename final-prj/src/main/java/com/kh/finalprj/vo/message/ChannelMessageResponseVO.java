package com.kh.finalprj.vo.message;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "특정 채널의 과거 메시지 목록 조회 결과")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ChannelMessageResponseVO {
	private List<MessageVO> messages;
	private boolean last;
}
