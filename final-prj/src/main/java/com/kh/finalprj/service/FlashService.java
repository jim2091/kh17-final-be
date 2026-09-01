package com.kh.finalprj.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.kh.finalprj.dto.EmpDto;

@Service
public class FlashService {
	
	
	//웹소켓 온라인/오프라인 상태 플래시 저장소 
	//한 사용자가 여러 기기에서 연결할 경우때문에 기기의 sessionId도 같이 저장
	
	private Map<String, EmpDto> onlineUsers = new ConcurrentHashMap<>();
	
	private final Map<String, Set<String>> sessions = new ConcurrentHashMap<>();
	
	public void enter(EmpDto empDto, String sessionId) {
		String empNo = String.valueOf(empDto.getEmpNo());
		
		sessions
	    .computeIfAbsent(empNo, key -> ConcurrentHashMap.newKeySet())
	    .add(sessionId);
		
		onlineUsers.put(empNo, empDto);
	}
	
	public void leave(EmpDto empDto, String sessionId) {
		String empNo = String.valueOf(empDto.getEmpNo());
		
		Set<String> userSessions = sessions.get(empNo);
		
		if (userSessions != null) {
		    userSessions.remove(sessionId);
	
		    if (userSessions.isEmpty()) {
		        sessions.remove(empNo);
		        
		        // 이때만 로그인 명단에서 지우기 
		        onlineUsers.remove(empNo);
		    }
		}
		
	}
	public List<EmpDto> list(){
		return new ArrayList<>(onlineUsers.values());
	}
	
	
	
	
	

}
