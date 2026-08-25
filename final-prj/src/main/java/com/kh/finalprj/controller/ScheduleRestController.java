package com.kh.finalprj.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;
import com.kh.finalprj.dao.ScheduleDao;
import com.kh.finalprj.dto.ScheduleDto;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.vo.schedule.ScheduleAddRequestVO;
import com.kh.finalprj.vo.schedule.ScheduleAddResponseVO;
import com.kh.finalprj.vo.schedule.ScheduleDeleteResponseVO;
import com.kh.finalprj.vo.schedule.ScheduleDetailResponseVO;
import com.kh.finalprj.vo.schedule.ScheduleEditRequestVO;
import com.kh.finalprj.vo.schedule.ScheduleEditResponseVO;
import com.kh.finalprj.vo.schedule.ScheduleEventVO;
import com.kh.finalprj.vo.schedule.ScheduleListResponseVO;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "일정 API")
@CommonsApiResponse

@RestController
@RequestMapping("/api/schedule")
public class ScheduleRestController {

	@Autowired
	private ScheduleDao scheduleDao;
	
	@ApiResponse(responseCode = "200", description = "일정 등록 성공")
	@PostMapping("/")
	public ScheduleAddResponseVO addEvent(@Valid @RequestBody ScheduleAddRequestVO request) throws BadRequestException {
		
		if(request.getScheduleEnd() != null
				&& !request.getScheduleStart().before(request.getScheduleEnd()))
			throw new BadRequestException();
		
		//아직 토큰 구현중
		//테스트로 임시값 사용
		int scheduleWriterNo = 1;
		
		int scheduleNo = scheduleDao.sequence();
		
		scheduleDao.insert(ScheduleDto.builder()
						.scheduleNo(scheduleNo)
						.projectNo(request.getProjectNo())
						.scheduleWriterNo(scheduleWriterNo)
						.scheduleTitle(request.getScheduleTitle())
						.scheduleContent(request.getScheduleContent())
						.scheduleStart(request.getScheduleStart())
						.scheduleEnd(request.getScheduleEnd())
						.schedulePlace(request.getSchedulePlace())
						.build());
		return ScheduleAddResponseVO.builder()
					.scheduleNo(scheduleNo)
				.build();
		
	}
	
	@ApiResponse(responseCode = "200", description = "일정 조회 성공")
	@GetMapping("/project/{projectNo}")
	public ScheduleListResponseVO list(@PathVariable int projectNo) {
		//토큰 완성되면 검사하고 안맞으면 내보내는 코드정도 추가할 듯.
		return ScheduleListResponseVO.builder()
	            .scheduleList(scheduleDao.selectList(projectNo))
	            .build();
	}
	
	@ApiResponse(responseCode = "200", description = "일정 상세 조회 성공")
	@GetMapping("/{scheduleNo}")
	public ScheduleDetailResponseVO detail(@PathVariable int scheduleNo) {
		//토큰 완성되면 검사하고 안맞으면 내보내는 코드정도 추가할 듯.
		ScheduleDetailResponseVO response = scheduleDao.selectOne(scheduleNo);
		
		if (response == null)
			throw new TargetNotfoundException();
		
		return response;
	}
	
	@ApiResponse(responseCode = "200", description = "일정 수정 성공")
	@PutMapping("/{scheduleNo}")
	public ScheduleEditResponseVO edit(@PathVariable int scheduleNo,
			@Valid @RequestBody ScheduleEditRequestVO request) throws BadRequestException {
		//토큰 완성되면 검사하고 안맞으면 내보내는 코드정도 추가할 듯
		//아 이건 본인이어야 하던가? 무튼
		
		if (!request.getScheduleStart().before(request.getScheduleEnd()))
			throw new BadRequestException();
		
		boolean result =  scheduleDao.update(
					ScheduleDto.builder()
							.scheduleNo(scheduleNo)
							.scheduleTitle(request.getScheduleTitle())
							.scheduleContent(request.getScheduleContent())
							.scheduleStart(request.getScheduleStart())
							.scheduleEnd(request.getScheduleEnd())
							.schedulePlace(request.getSchedulePlace())
						.build()
				);
		if (result == false)
			throw new TargetNotfoundException();
		
		return ScheduleEditResponseVO.builder()
					.scheduleNo(scheduleNo)
				.build();
	}
	
	@ApiResponse(responseCode = "200", description = "일정 삭제 성공")
	@DeleteMapping("/{scheduleNo}")
	public ScheduleDeleteResponseVO delete(@PathVariable int scheduleNo) {
		//토큰 완성되면 검사하고 안맞으면 내보내는 코드정도 추가할 듯
		//아 이건 본인이어야 하던가? 무튼
		
		boolean result = scheduleDao.delete(scheduleNo);
		if (result == false)
			throw new TargetNotfoundException();
		
		return ScheduleDeleteResponseVO.builder()
					.scheduleNo(scheduleNo)
				.build();
	}
}
