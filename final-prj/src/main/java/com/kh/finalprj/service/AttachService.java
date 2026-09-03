package com.kh.finalprj.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.kh.finalprj.dto.AttachDto;
import com.kh.finalprj.vo.attach.AttachInfoVO;

public interface AttachService {

	int save(int projectNo, MultipartFile attach, String uploader, String source)
			throws IllegalStateException, IOException;

	void delete(Integer attachNo, String uploader);

	AttachInfoVO load(int attachNo) throws IOException;

	List<AttachDto> list(int projectNo);

	List<AttachDto> list(int projectNo, String keyword);
	
	//회원 프로필 사진 연결용(민영작성)
	int save(MultipartFile attach, String empName, String sourse)
			throws IllegalStateException, IOException;
}