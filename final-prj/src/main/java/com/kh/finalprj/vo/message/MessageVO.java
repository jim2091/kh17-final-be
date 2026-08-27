package com.kh.finalprj.vo.message;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//메세지 기본 VO
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageVO {
	private int no;
    private int channelNo;
    private Integer projectMemberNo;
    private int empNo;
    private String senderName;
    private String content;
    private String type;
    private Timestamp ctime;
    private Timestamp utime;
    private String deleted;
}
