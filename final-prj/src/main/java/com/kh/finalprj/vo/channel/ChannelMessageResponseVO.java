package com.kh.finalprj.vo.channel;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "채팅 채널 메시지 응답 데이터")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ChannelMessageResponseVO {
	private List<MessageVO> messages;
	private boolean last;
}
