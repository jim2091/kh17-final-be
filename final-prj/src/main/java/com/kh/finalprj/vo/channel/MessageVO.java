package com.kh.finalprj.vo.channel;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//DB에 저장할 데이터
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageVO {
	private int no;
    private int channelNo;
    private Integer projectMemberNo;
    private String senderName;
    private String content;
    private String type;
    private Timestamp ctime;
    private Timestamp utime;
    private String deleted;
}
