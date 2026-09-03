package com.kh.finalprj.dto;

import java.sql.Timestamp;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "업무 첨부파일 DTO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TaskFileDto {
    private int taskNo;
    private int attachNo;
    private Timestamp taskFileCtime; // 
}