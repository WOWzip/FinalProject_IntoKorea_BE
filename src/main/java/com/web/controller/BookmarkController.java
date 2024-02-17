package com.web.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.domain.Bookmark;
import com.web.service.BookmarkService;

// BookmarkController

@RequestMapping("/mypage")
@RestController
public class BookmarkController {
	
	@Autowired
	private BookmarkService bookmarkService;
	
	// 즐겨찾기 저장
	@PostMapping("/bookmark")
	public int bookmark(@RequestBody Bookmark bookmark) {
		System.out.println(bookmark);
		
		bookmarkService.registerBookmark(bookmark);
		
		return 1;
	}
	
	
	// 즐겨찾기 목록
	@PostMapping("/bookmarkList")
	public List<Bookmark> BookmarkList(@RequestBody Bookmark bookmark){
		System.out.println("email: " + bookmark);
		
		List<Bookmark> bookmarklist = bookmarkService.BookmarkList(bookmark.getEmail());
		
		Map<String, Object> list = new HashMap<>();
		
		
		return bookmarklist;
	}
	
	// 즐겨찾기 색상
	@PostMapping("bookmarkColor")
	public int BookmarkColor(@RequestBody Bookmark bookmark) {
		System.out.println("color : " + bookmark);
		
		int su = bookmarkService.BookmarkColor(bookmark);
		
		return su;
	}
	
	// 즐겨찾기 제거
	@PostMapping("bookmarkDelete")
	public int BookmarkDelete(@RequestBody Bookmark bookmark) {
		System.out.println(("delete : " + bookmark));
		bookmarkService.bookmarkDelete(bookmark);
		
		return 0;
	}

}
















