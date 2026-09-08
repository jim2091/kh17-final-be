package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.service.ProjectPresenceService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.websocket.presence.vo.PresenceResponseVO;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name="presence API")
@CommonsApiResponse

@RestController
@RequestMapping("/api/presence")
public class PresenceRestController {
	@Autowired
	private ProjectPresenceService projectPresenceService;
	
	@GetMapping("/project/{projectNo}")
	public List<PresenceResponseVO> list(
			@PathVariable int projectNo,
			@CurrentUser TokenParseResponseVO parseVO) {
		
		return projectPresenceService.list(projectNo, parseVO.getEmpNo());
	}
}
