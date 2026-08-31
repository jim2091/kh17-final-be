package com.kh.finalprj.vo.note;

import java.util.List;

import com.kh.finalprj.dto.NoteDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "노트 목록 조회 응답 VO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NoteListResponseVO {
	private List<NoteDto> noteList;
	private boolean last;
}
