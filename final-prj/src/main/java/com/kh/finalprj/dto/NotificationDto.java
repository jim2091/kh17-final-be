package com.kh.finalprj.dto;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "알림 DTO")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NotificationDto {
    private int notificationNo;
    private int notificationReceiver;
    private Integer projectNo;
    private String notificationType;
    private Integer notificationTarget;
    private String notificationUrl;
    private String notificationContent;
    private String notificationRead;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp notificationCtime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp notificationRtime;
}