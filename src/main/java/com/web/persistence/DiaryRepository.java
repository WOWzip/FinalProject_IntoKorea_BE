package com.web.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.domain.Diary;

public interface DiaryRepository extends JpaRepository<Diary, Long>{
	
	// 다이어리 삭제
	void deleteBySeq(Long seq);
	
	// 다이어리 수정
	Diary findBySeq(Long seq);
	
    // 제목으로 다이어리 검색
    List<Diary> findByDtitleContaining(String keyword);

    // 테마로 다이어리 검색
    List<Diary> findByThemeContaining(String keyword);

    // 내용으로 다이어리 검색
    List<Diary> findByDcontentContaining(String keyword);
}


