package com.kh.finalprj.vo.dm;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DmMessageResponseVO {

	private List<DmMessageVO> messages;
	private boolean last;

}