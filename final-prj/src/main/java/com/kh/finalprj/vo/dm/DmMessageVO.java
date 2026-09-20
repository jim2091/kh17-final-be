package com.kh.finalprj.vo.dm;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DmMessageVO {

	private int no;
	private int roomNo;

	private int senderNo;
	private String senderName;

	private String content;

	private Timestamp ctime;

}