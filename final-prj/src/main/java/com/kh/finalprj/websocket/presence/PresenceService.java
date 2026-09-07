package com.kh.finalprj.websocket.presence;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.finalprj.dao.EmpDao;
import com.kh.finalprj.dto.EmpDto;

@Service
public class PresenceService {

	@Autowired
	private EmpDao empDao;
	
	//empNo별로 현재 연결되어 있는 WebSocket sessionId들을 저장
	private final Map<Integer, Set<String>> sessions = new ConcurrentHashMap<>();
	
	public boolean enter(int empNo, String sessionId) {
		Set<String> userSessions = sessions.get(empNo);
		
		//첫 웹소켓 연결일 때
		if(userSessions == null) {
			userSessions = ConcurrentHashMap.newKeySet();
			sessions.put(empNo, userSessions);
			
			empDao.updatePresence(empNo, PresenceStatus.ONLINE.name());

			userSessions.add(sessionId);
			
			return true;
		}
		
		//이미 다른 웹소켓 연결이 존재함
		userSessions.add(sessionId);
		
		return false;

	}
	
	public boolean leave(int empNo, String sessionId) {
		Set<String> userSessions = sessions.get(empNo);
		
		if(userSessions == null) {
			return false;
		}
		
		userSessions.remove(sessionId);
		
		//마지막 웹소켓 연결일 끊어질 때
		if(userSessions.isEmpty()) {
			sessions.remove(empNo);
			
			empDao.updatePresence(empNo, PresenceStatus.OFFLINE.name());
			
			return true;
		}
		
		//다른 연결이 아직 남아 있음
		return false;
	}
	
	//웹소켓 연결 존재 여부
	public boolean isOnline(int empNo) {
		return sessions.containsKey(empNo);
	}
	
	public boolean changeStatus(int empNo, PresenceStatus newStatus) {
		if(newStatus == null) {
			return false;
		}
		
		if(!isOnline(empNo)) {
			return false;
		}
		
		if(newStatus == PresenceStatus.OFFLINE) {
			return false;
		}
		
		EmpDto empDto = empDao.selectOne(empNo);
		
		//이미 같은 상태라면 변경할 필요 없음
		if(newStatus.name().equals(empDto.getEmpPresence())) {
			return false;
		}

		empDao.updatePresence(empNo, newStatus.name());
		
		return true;
	}
	
	public PresenceStatus getStatus(int empNo) {
		if(!isOnline(empNo)) {
			return PresenceStatus.OFFLINE;
		}
		
		EmpDto empDto = empDao.selectOne(empNo);
		//혹시 null일때 에러 안나게 온라인으로 반환
		if(empDto.getEmpPresence() == null)
			return PresenceStatus.ONLINE;
		
		return PresenceStatus.valueOf(empDto.getEmpPresence());
	}
}
