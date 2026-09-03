package com.kh.finalprj.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.annotation.CurrentUser;
import com.kh.finalprj.dto.SearchDto;
import com.kh.finalprj.service.SearchService;
import com.kh.finalprj.vo.jwt.TokenParseResponseVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchRestController {

    private final SearchService searchService;

    @GetMapping
    public SearchDto search(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "all") String filter,
            @CurrentUser TokenParseResponseVO parseVO
    ) {

        int empNo = parseVO.getEmpNo();

        return searchService.search(keyword, filter, empNo);
    }
}