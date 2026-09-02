package com.kh.finalprj.vo.task;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name="첨부파일 상세 응답 VO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TaskFileResponseVO {
    private int taskNo;
    
    private String attachName;
    private String attachType;
    private long attachSize;
    
    private String fileRole;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp ctime; 
}
