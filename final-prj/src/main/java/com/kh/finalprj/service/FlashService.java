package com.kh.finalprj.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.kh.finalprj.dto.EmpDto;

@Service
public class FlashService {
	
	
	//웹소켓 온라인/오프라인 상태 플래시 저장소 
	
	private Map<String, EmpDto> onlineUsers = new ConcurrentHashMap<>();
	
	public void enter(EmpDto empDto) {
		String empNo = String.valueOf(empDto.getEmpNo());
		onlineUsers.put(empNo, empDto);
	}
	
	public void leave(EmpDto empDto) {
		String empNo = String.valueOf(empDto.getEmpNo());
		onlineUsers.remove(empNo);
	}
	public List<EmpDto> list(){
		return new ArrayList<>(onlineUsers.values());
	}

}
