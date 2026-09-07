package com.kh.finalprj.websocket.presence.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kh.finalprj.websocket.presence.PresenceStatus;

import lombok.Data;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class PresenceChangeRequestVO {
	private PresenceStatus status;
}
