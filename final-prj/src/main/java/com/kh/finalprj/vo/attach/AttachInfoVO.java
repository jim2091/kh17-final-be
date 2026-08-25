package com.kh.finalprj.vo.attach;

import org.springframework.core.io.Resource;

import com.kh.finalprj.dto.AttachDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AttachInfoVO {
	private AttachDto attachDto;
	private Resource resource;
}