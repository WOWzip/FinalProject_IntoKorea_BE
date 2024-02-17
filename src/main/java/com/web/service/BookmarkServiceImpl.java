package com.web.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.domain.Bookmark;
import com.web.domain.Member;
import com.web.persistence.BookmarkRepository;


@Service
public class BookmarkServiceImpl implements BookmarkService {
	
	
	@Autowired
	private BookmarkRepository bookmarkRepository;

	@Override
	public void registerBookmark(Bookmark bookmark) {
		
		bookmarkRepository.save(bookmark);
		
	}
	
	// 즐겨찾기 목록
	@Override
	public List<Bookmark> BookmarkList(String email) {
		
		List<Bookmark> optional = bookmarkRepository.findByEmail(email);
		
		System.out.println(optional);
		
		return optional;
	}
	
	
	@Override
	public int BookmarkColor(Bookmark bookmark) {
		String email = bookmark.getEmail();
		String contentid = bookmark.getContentid();
		System.out.println(email + " / " + contentid);
		
		int check = bookmarkRepository.bookmarkCheck(email, contentid);
		System.out.println("여기 : "  + check);
		
		if(check >= 1) {
			return 1;
		} else {
			return 0;
		}
	}
	
	@Override
	public void bookmarkDelete(Bookmark bookmark) {
		
		String delete = bookmark.getEmailcontentid();
		
		bookmarkRepository.delete(bookmark);
		
		
	}
	
} 
