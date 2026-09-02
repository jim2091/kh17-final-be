package com.kh.finalprj.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.finalprj.dto.SearchDto;
import com.kh.finalprj.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchRestController {

    private final SearchService searchService;


    // ========================================
    // 통합 검색
    // ========================================

    @GetMapping
    public SearchDto search(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "all") String filter
    ) {

        return searchService.search(keyword, filter);
    }

}