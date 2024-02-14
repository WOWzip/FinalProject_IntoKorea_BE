

package com.web.service;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.domain.Diary;

@Service
public interface DiaryService {
	
	// 다이어리 작성
	public Diary save(Diary diary);

	// 다이어리 작성 + 이미지
	public Diary saveDiary(Diary diary);
	public void attachimage(Diary diary, MultipartFile image) throws Exception;
	
	// 다이어리 리스트
	public List<Diary> getAllDiarys();
	
	// 다이어리 검색
	public List<Diary> searchDiary(String searchType, String keyword);
	
	// 다이어리 삭제
	public void deleteDiaryBySeq(Long seq);
	
	// 다이어리 수정
	public Diary updateDiary(Diary diary);
	public Diary getDiaryBySeq(Long seq);
	Diary attachImage(Diary diary, MultipartFile image) throws Exception;
	
	// 다이어리 상세보기
	public Diary getDiaryDetail(Long seq);
	
	//visitdate
	Diary saveVisitDate(Long seq, Date visitDate);
}
