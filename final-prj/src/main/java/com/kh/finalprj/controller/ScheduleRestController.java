package com.kh.finalprj.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CommonsApiResponse;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "일정 API")
@CommonsApiResponse

@RestController
@RequestMapping("/api/schedule")
public class ScheduleRestController {

	//여기 작성 중
}
