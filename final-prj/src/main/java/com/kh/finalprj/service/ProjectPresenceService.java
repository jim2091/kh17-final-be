package com.kh.finalprj.service;

import java.util.List;

import com.kh.finalprj.websocket.presence.vo.PresenceResponseVO;

public interface ProjectPresenceService {
	List<PresenceResponseVO> list(int projectNo, int empNo);
}
