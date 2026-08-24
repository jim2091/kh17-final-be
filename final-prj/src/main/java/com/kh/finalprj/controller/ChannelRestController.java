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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.dao.ChannelDao;
import com.kh.finalprj.dto.ChannelDto;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.service.ChannelService;
import com.kh.finalprj.vo.channel.ChannelCreateRequestVO;
import com.kh.finalprj.vo.channel.ChannelDeleteRequestVO;
import com.kh.finalprj.vo.channel.ChannelUpdateRequestVO;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "채널 관리 API")
@RestController
@RequestMapping("/api/channel")
public class ChannelRestController {
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private ChannelService channelService;
	
	@ApiResponse(responseCode = "200", description = "채널 생성 성공")
	@PostMapping(value = "/",  produces = MediaType.APPLICATION_JSON_VALUE)
	public void createChannel(
		@PathVariable int projectNo,
		@Valid @RequestBody ChannelCreateRequestVO request,
		@CurrentUser TokenParseResponseVO parseVO
	) {
		ChannelDto channelDto = ChannelDto.builder()
				.projectNo(projectNo)
				.chatChannelName(request.getChatChannelName())
			.build();
			
		channelService.create(channelDto);
	}

	@ApiResponse(responseCode = "200", description = "채널 목록 조회 성공")
	@GetMapping(value = "/",  produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ChannelDto> list(@PathVariable int projectNo) {
		return channelService.list(projectNo);
	}
	
	@ApiResponse(responseCode = "200", description = "채널 상세 조회 성공")
	@GetMapping(value = "/{channelNo}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ChannelDto detail(
		@RequestParam int projectNo,
		@PathVariable int channelNo
	) {
		ChannelDto channelDto = channelDao.selectOne(projectNo, channelNo);
		
		if(channelDto == null) {
			throw new TargetNotfoundException();
		}
		
		return channelDto;
	}
	
	@ApiResponse(responseCode = "200", description = "채널 삭제 성공")
	@DeleteMapping(value = "/{channelNo}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteChannel(
		@PathVariable int channelNo,
		@RequestBody ChannelDeleteRequestVO request
	) {
		channelService.delete(request.getProjectNo(), channelNo);
	}
	
	@ApiResponse(responseCode = "200", description = "채널 수정 성공")
	@PutMapping(value = "/{channelNo}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateChannel(
		@PathVariable int channelNo,
		@Valid @RequestBody ChannelUpdateRequestVO request
	) {
		ChannelDto channelDto = ChannelDto.builder()
				.projectNo(request.getProjectNo())
				.chatChannelNo(channelNo)
				.chatChannelName(request.getChatChannelName())
			.build();
		
		channelService.update(channelDto);
	}
}
