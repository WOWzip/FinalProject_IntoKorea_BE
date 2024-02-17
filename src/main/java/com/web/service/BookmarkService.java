package com.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.web.domain.Bookmark;

@Service
public interface BookmarkService {
	
	// 즐겨찾기 등록
	public void registerBookmark(Bookmark bookmark);
	
	// 즐겨찾기 목록
	public List<Bookmark> BookmarkList(String email);
	
	// 즐겨찾기 색상
	public int BookmarkColor(Bookmark bookmark);
	
	public void bookmarkDelete(Bookmark bookmark);

}
