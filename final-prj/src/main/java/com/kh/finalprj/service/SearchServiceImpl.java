package com.kh.finalprj.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.finalprj.dao.SearchDao;
import com.kh.finalprj.dto.SearchDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchServiceImpl implements SearchService {

	private final SearchDao searchDao;

	@Override
	public SearchDto search(String keyword, String filter) {

		// ========================================
		// 검색어 정리
		// ========================================

		if (keyword == null) {
			keyword = "";
		}

		keyword = keyword.trim();

		// ========================================
		// 필터 정리
		// ========================================

		if (filter == null || filter.trim().isEmpty()) {
			filter = "all";
		}

		filter = filter.trim().toLowerCase();

		// ========================================
		// 결과 객체 생성
		// ========================================

		SearchDto result = new SearchDto();

		result.setKeyword(keyword);
		result.setFilter(filter);

		/*
		 * 모든 카테고리를 먼저 빈 배열로 만들어 둡니다.
		 *
		 * 따라서 검색 결과가 없어도
		 *
		 * users: [] projects: [] tasks: [] records: [] notes: [] files: []
		 *
		 * 형태로 항상 내려갑니다.
		 */

		result.setUsers(new ArrayList<>());
		result.setProjects(new ArrayList<>());
		result.setTasks(new ArrayList<>());
		result.setRecords(new ArrayList<>());
		result.setNotes(new ArrayList<>());
		result.setFiles(new ArrayList<>());

		// ========================================
		// 검색어가 없는 경우
		// ========================================

		if (keyword.isEmpty()) {
			return result;
		}

		// ========================================
		// 전체 검색
		// ========================================

		if ("all".equals(filter)) {

			result.setUsers(searchDao.searchMembers(keyword));

			result.setProjects(searchDao.searchProjects(keyword));

			result.setTasks(searchDao.searchTasks(keyword));

			result.setFiles(searchDao.searchFiles(keyword));

			return result;
		}

		// ========================================
		// 사용자
		// ========================================

		if ("user".equals(filter) || "users".equals(filter)) {

			result.setUsers(searchDao.searchMembers(keyword));

			return result;
		}

		// ========================================
		// 프로젝트
		// ========================================

		if ("project".equals(filter) || "projects".equals(filter)) {

			result.setProjects(searchDao.searchProjects(keyword));

			return result;
		}

		// ========================================
		// 업무
		// ========================================

		if ("task".equals(filter) || "tasks".equals(filter)) {

			result.setTasks(searchDao.searchTasks(keyword));

			return result;
		}

		// ========================================
		// Records
		// ========================================

		if ("record".equals(filter) || "records".equals(filter)) {

			/*
			 * 현재 records 테이블이 없으므로 빈 배열을 그대로 반환합니다.
			 */

			return result;
		}

		// ========================================
		// 노트
		// ========================================

		if ("note".equals(filter) || "notes".equals(filter)) {

			/*
			 * 현재 note 테이블이 없으므로 빈 배열을 그대로 반환합니다.
			 */

			return result;
		}

		// ========================================
		// 파일
		// ========================================

		if ("file".equals(filter) || "files".equals(filter)) {

			result.setFiles(searchDao.searchFiles(keyword));

			return result;
		}

		// ========================================
		// 잘못된 필터
		// ========================================

		/*
		 * 정의되지 않은 필터가 들어오면 전체 검색으로 처리합니다.
		 */

		result.setUsers(searchDao.searchMembers(keyword));

		result.setProjects(searchDao.searchProjects(keyword));

		result.setTasks(searchDao.searchTasks(keyword));

		result.setFiles(searchDao.searchFiles(keyword));

		return result;
	}

}