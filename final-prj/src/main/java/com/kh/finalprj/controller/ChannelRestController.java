package com.kh.finalprj.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.dao.ChannelDao;
import com.kh.finalprj.dao.ProjectMemberDao;
import com.kh.finalprj.dto.ChannelDto;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.service.ChannelService;
import com.kh.finalprj.service.JwtService;
import com.kh.finalprj.service.MessageService;
import com.kh.finalprj.service.ProjectPermissionService;
import com.kh.finalprj.vo.channel.ChannelCreateRequestVO;
import com.kh.finalprj.vo.channel.ChannelDeleteRequestVO;
import com.kh.finalprj.vo.channel.ChannelUpdateRequestVO;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

//채널 CRUD + 채널의 메시지 목록 조회
@Tag(name = "채널 API")
@RestController
@RequestMapping("/api/channel")
public class ChannelRestController {
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private ProjectPermissionService projectPermissionService;
	
	
	//채널 생성
	@ApiResponse(responseCode = "200", description = "채널 생성 성공")
	@PostMapping("/")
	public void createChannel(
		@Valid @RequestBody ChannelCreateRequestVO request,
		@CurrentUser TokenParseResponseVO parseVO
	) {
		
		ChannelDto channelDto = ChannelDto.builder()
				.projectNo(request.getProjectNo())
				.chatChannelName(request.getChatChannelName())
			.build();
			
		channelService.create(channelDto, parseVO.getEmpNo());
	}

	
	//채널 목록
	@ApiResponse(responseCode = "200", description = "채널 목록 조회 성공")
	@GetMapping("/project/{projectNo}")
	public List<ChannelDto> list(
			@PathVariable int projectNo,
			@CurrentUser TokenParseResponseVO parseVO
	) {
		return channelService.list(projectNo, parseVO.getEmpNo());
	}
	
	
	//채널 상세
	@ApiResponse(responseCode = "200", description = "채널 상세 조회 성공")
	@GetMapping("/project/{projectNo}/{channelNo}")
	public ChannelDto detail(
			@PathVariable int projectNo,
	        @PathVariable int channelNo,
	        @CurrentUser TokenParseResponseVO parseVO
		) {
		//프로젝트 멤버인지 검사 코드 추가
		projectPermissionService.checkMember(projectNo, parseVO.getEmpNo());
		ChannelDto channelDto = 
				channelDao.selectOne(projectNo, channelNo);
		
		if(channelDto == null) {
			throw new TargetNotfoundException();
		}
		
		return channelDto;
	}
	
	
	//채널 삭제
	@ApiResponse(responseCode = "200", description = "채널 삭제 성공")
	@DeleteMapping(value = "/{channelNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteChannel(
		@PathVariable int channelNo,
		@RequestBody ChannelDeleteRequestVO request,
		@CurrentUser TokenParseResponseVO parseVO
	) {
		channelService.delete(
				request.getProjectNo(), channelNo, parseVO.getEmpNo());
	}
	
	
	//채널 수정
	@ApiResponse(responseCode = "200", description = "채널 수정 성공")
	@PutMapping(value = "/{channelNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateChannel(
		@PathVariable int channelNo,
		@Valid @RequestBody ChannelUpdateRequestVO request,
		@CurrentUser TokenParseResponseVO parseVO
	) {
		ChannelDto channelDto = ChannelDto.builder()
				.projectNo(request.getProjectNo())
				.chatChannelNo(channelNo)
				.chatChannelName(request.getChatChannelName())
			.build();
		
		channelService.update(channelDto, parseVO.getEmpNo());
	}
}
