package com.kh.finalprj.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.finalprj.dao.AttachDao;
import com.kh.finalprj.dao.NoteCommentDao;
import com.kh.finalprj.dao.NoteDao;
import com.kh.finalprj.dao.NoteFileDao;
import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.dto.NoteCommentFileDto;
import com.kh.finalprj.dto.NoteFileDto;
import com.kh.finalprj.vo.note.NoteCommentDetailResponseVO;
import com.kh.finalprj.vo.note.NoteDetailResponseVO;
import com.kh.finalprj.vo.note.NoteFileResponseVO;

@Service
public class NoteFileServiceImpl implements NoteFileService {

	@Autowired
	private NoteFileDao noteFileDao;

	@Autowired
	private AttachService attachService;

	@Autowired
	private AttachDao attachDao;

	@Autowired
	private NoteDao noteDao;

	@Autowired(required = false)
	private NoteCommentDao noteCommentDao;

	@Override
	@Transactional
	public NoteFileResponseVO uploadNoteFile(int noteNo, int projectNo, String uploader, MultipartFile file)
			throws IOException {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("업로드할 파일이 없습니다.");
		}

		String validUploader = (uploader != null && !uploader.isBlank()) ? uploader : "SYSTEM";

		int validProjectNo = projectNo;
		if (validProjectNo <= 0) {
			NoteDetailResponseVO note = noteDao.selectOne(noteNo);
			if (note != null) {
				validProjectNo = note.getProjectNo();
			}
		}

		int attachNo = attachService.save(validProjectNo, file, validUploader, "NOTE");

		NoteFileDto noteFileDto = NoteFileDto.builder()
				.noteNo(noteNo)
				.attachNo(attachNo)
				.build();
		noteFileDao.addNoteFile(noteFileDto);

		return NoteFileResponseVO.builder()
				.attachNo(attachNo)
				.attachName(file.getOriginalFilename())
				.attachType(file.getContentType())
				.attachSize(file.getSize())
				.build();
	}

	@Override
	@Transactional(readOnly = true)
	public List<NoteFileResponseVO> getNoteFies(int noteNo) {
		return noteFileDao.selectFileByNoteNo(noteNo);
	}

	@Override
	@Transactional
	public boolean removeNoteFile(int noteNo, int attachNo, String uploader) {
		boolean deleted = noteFileDao.deleteNoteFile(noteNo, attachNo);
		if (deleted) {
			String targetUploader = resolveUploader(attachNo, uploader);
			attachService.delete(attachNo, targetUploader);
		}
		return deleted;
	}

	@Override
	@Transactional
	public NoteFileResponseVO uploadCommentFile(int noteCommentNo, int projectNo, String uploader, MultipartFile file)
			throws IOException {
		if (file == null || file.isEmpty()) {
			throw new IllegalArgumentException("업로드할 파일이 없습니다.");
		}

		String validUploader = (uploader != null && !uploader.isBlank()) ? uploader : "SYSTEM";

		int validProjectNo = projectNo;
		if (validProjectNo <= 0 && noteCommentDao != null) {
			try {
				NoteCommentDetailResponseVO comment = noteCommentDao.selectOne(noteCommentNo);
				if (comment != null) {
					NoteDetailResponseVO note = noteDao.selectOne(comment.getNoteNo());
					if (note != null) {
						validProjectNo = note.getProjectNo();
					}
				}
			} catch (Exception ignored) {
			}
		}

		int attachNo = attachService.save(validProjectNo, file, validUploader, "NOTE_COMMENT");

		NoteCommentFileDto commentFileDto = NoteCommentFileDto.builder()
				.noteCommentNo(noteCommentNo)
				.attachNo(attachNo)
				.build();
		noteFileDao.addCommentFile(commentFileDto);

		return NoteFileResponseVO.builder()
				.attachNo(attachNo)
				.attachName(file.getOriginalFilename())
				.attachType(file.getContentType())
				.attachSize(file.getSize())
				.build();
	}

	@Override
	@Transactional(readOnly = true)
	public List<NoteFileResponseVO> getCommentFiles(int noteCommentNo) {
		return noteFileDao.selectFileByCommentNo(noteCommentNo);
	}

	@Override
	@Transactional
	public boolean removeCommentFile(int noteCommentNo, int attachNo, String uploader) {
		boolean deleted = noteFileDao.deleteCommentFile(noteCommentNo, attachNo);
		if (deleted) {
			String targetUploader = resolveUploader(attachNo, uploader);
			attachService.delete(attachNo, targetUploader);
		}
		return deleted;
	}

	private String resolveUploader(int attachNo, String requestUploader) {
		if (requestUploader != null && !requestUploader.isBlank() && !"SYSTEM".equals(requestUploader)) {
			return requestUploader;
		}
		AttachDto attachDto = attachDao.selectOne(attachNo);
		if (attachDto != null && attachDto.getAttachUploader() != null && !attachDto.getAttachUploader().isBlank()) {
			return attachDto.getAttachUploader();
		}
		return "SYSTEM";
	}
}