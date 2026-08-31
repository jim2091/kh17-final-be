package com.kh.finalprj.controller;

import java.util.List;

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
import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.dao.NoteDao;
import com.kh.finalprj.dto.NoteDto;
import com.kh.finalprj.error.GetOutException;
import com.kh.finalprj.error.TargetNotfoundException;
import com.kh.finalprj.service.ProjectPermissionService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;
import com.kh.finalprj.vo.note.NoteAddRequestVO;
import com.kh.finalprj.vo.note.NoteAddResponseVO;
import com.kh.finalprj.vo.note.NoteDeleteResponseVO;
import com.kh.finalprj.vo.note.NoteEditRequestVO;
import com.kh.finalprj.vo.note.NoteEditResponseVO;
import com.kh.finalprj.vo.note.NoteListRequestVO;
import com.kh.finalprj.vo.note.NoteListResponseVO;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name="노트 API")
@CommonsApiResponse
@RestController
@RequestMapping("/api/note")
public class NoteRestController {

	@Autowired
	private NoteDao noteDao;
	@Autowired
	private ProjectPermissionService projectPermissionService;
	
	@ApiResponse(responseCode = "200", description = "노트 목록 조회 성공")
	@PostMapping("/project/{projectNo}/list")
	public NoteListResponseVO list(
			@PathVariable int projectNo,
			@Valid @RequestBody NoteListRequestVO request,
			@CurrentUser TokenParseResponseVO parseVO) {
		
		projectPermissionService.checkMember(projectNo, parseVO.getEmpNo());
		
		request.setProjectNo(projectNo);
		
		List<NoteDto> noteList = noteDao.selectList(request);
		int count = noteDao.count(request);
		
		boolean last = noteList.size() >= count;
		
		return NoteListResponseVO.builder()
					.noteList(noteList)
					.last(last)
				.build();
	}
	
	@ApiResponse(responseCode = "200", description = "노트 등록 성공")
	@PostMapping("/project/{projectNo}")
	public NoteAddResponseVO add(
			@PathVariable int projectNo,
			@Valid @RequestBody NoteAddRequestVO request,
			@CurrentUser TokenParseResponseVO parseVO) {
		
		int memberNo = projectPermissionService.findProjectMemberNo(projectNo, parseVO.getEmpNo());
		
		int noteNo = noteDao.sequence();
		
		noteDao.insert(NoteDto.builder()
					.noteNo(noteNo)
					.projectNo(projectNo)
					.noteTitle(request.getNoteTitle())
					.noteContent(request.getNoteContent())
					.noteWriterNo(memberNo)
				.build());
		return NoteAddResponseVO.builder()
					.noteNo(noteNo)
				.build();
	}
	
	@ApiResponse(responseCode = "200", description = "노트 상세 조회 성공")
	@GetMapping("/{noteNo}")
	public NoteDto detail(
			@PathVariable int noteNo,
			@CurrentUser TokenParseResponseVO parseVO) {
		NoteDto noteDto = noteDao.selectOne(noteNo);
		if (noteDto == null)
			throw new TargetNotfoundException();
		
		projectPermissionService.checkMember(noteDto.getProjectNo(), parseVO.getEmpNo());
		
		return noteDto;
	}
	
	@ApiResponse(responseCode = "200", description = "노트 수정 성공")
	@PutMapping("/{noteNo}")
	public NoteEditResponseVO edit(
			@PathVariable int noteNo,
			@Valid @RequestBody NoteEditRequestVO request,
			@CurrentUser TokenParseResponseVO parseVO) {
		
		NoteDto noteDto = noteDao.selectOne(noteNo);
		if(noteDto == null)
			throw new TargetNotfoundException();
		
		int memberNo = projectPermissionService.findProjectMemberNo(noteDto.getProjectNo(), parseVO.getEmpNo());
		
		if(memberNo != noteDto.getNoteWriterNo())
			throw new GetOutException();
		
		boolean result = noteDao.update(NoteDto.builder()
					.noteNo(noteNo)
					.noteTitle(request.getNoteTitle())
					.noteContent(request.getNoteContent())
				.build());
		
		if(result == false)
			throw new TargetNotfoundException();
		
		return NoteEditResponseVO.builder()
					.noteNo(noteNo)
				.build();
	}
	
	@ApiResponse(responseCode = "200", description = "노트 삭제 성공")
	@DeleteMapping("/{noteNo}")
	public NoteDeleteResponseVO delete(
			@PathVariable int noteNo,
			@CurrentUser TokenParseResponseVO parseVO) {
		
		NoteDto noteDto = noteDao.selectOne(noteNo);
		
		if(noteDto == null)
			throw new TargetNotfoundException();
		
		int memberNo = projectPermissionService.findProjectMemberNo(noteDto.getProjectNo(), parseVO.getEmpNo());
		
		if(memberNo != noteDto.getNoteWriterNo()) {
			projectPermissionService.checkOwnerOrManager(noteDto.getProjectNo(), parseVO.getEmpNo());
		}
		
		boolean result = noteDao.delete(noteNo);
		
		if(result == false)
			throw new TargetNotfoundException();
		
		return NoteDeleteResponseVO.builder()
					.noteNo(noteNo)
				.build();
		
	}
}
