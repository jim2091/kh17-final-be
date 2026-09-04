package com.kh.finalprj.websocket.presence;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class PresenceService {
	//empNo별로 현재 연결되어 있는 WebSocket sessionId들을 저장
	private final Map<Integer, Set<String>> sessions = new ConcurrentHashMap<>();
	
	private final Map<Integer, PresenceStatus> status = new ConcurrentHashMap<>();
	
	public void enter(int empNo, String sessionId) {
		Set<String> userSessions = sessions.get(empNo);
		
		if(userSessions == null) {
			userSessions = ConcurrentHashMap.newKeySet();
			sessions.put(empNo, userSessions);
		}
		userSessions.add(sessionId);
		
		//상태값이 아예 없을때만 ONLINE 넣어라.
		//그냥 put으로 넣으면 AWAY상태가 그냥 강제로 ONLINE이 됨(탭 하나 새로 열거나 했을때도)
		status.putIfAbsent(empNo, PresenceStatus.ONLINE);
	}
	
	public void leave(int empNo, String sessionId) {
		Set<String> userSessions = sessions.get(empNo);
		
		if(userSessions == null) {
			return;
		}
		
		userSessions.remove(sessionId);
		
		if(userSessions.isEmpty()) {
			sessions.remove(empNo);
		}
	}
	
	//연결이 살아있느냐의 판정이지 실제 status는 AWAY도 있기에 이걸로 판정 불가
	public boolean isOnline(int empNo) {
		return sessions.containsKey(empNo);
	}
}
