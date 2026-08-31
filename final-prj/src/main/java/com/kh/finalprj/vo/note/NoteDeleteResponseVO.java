package com.kh.finalprj.vo.note;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "노트 삭제 응답 VO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NoteDeleteResponseVO {
	private int noteNo;
}
