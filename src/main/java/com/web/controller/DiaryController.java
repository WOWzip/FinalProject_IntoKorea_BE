
package com.web.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.web.domain.Diary;
import com.web.service.DiaryService;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RequestMapping("/mypage")
public class DiaryController {

	@Autowired
	private DiaryService diaryService;

	// 다이어리 작성
	@PostMapping("/TravelDiary1")
	public ResponseEntity<String> saveDiary(@RequestBody Diary diary) {
			diaryService.save(diary);
			System.out.println(diary);
			return ResponseEntity.ok("다이어리가 성공적으로 저장되었습니다.");
			
	}
	
	// 다이어리 작성 + 이미지 업로드
	@Transactional
	@PostMapping(value = "/TravelDiary", consumes = "multipart/form-data")
	public Diary submitDiary(
			@RequestPart(name ="image", required = false) MultipartFile image,
			@RequestPart(name = "Dtitle") String Dtitle, 
			@RequestPart(name = "Dcontent") String Dcontent,
			@RequestPart(name = "Location") String Location,
			@RequestPart(name = "rating") String rating,
			@RequestPart(name = "theme") String theme
//			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // 날짜 형식 지정
//			@RequestPart(name = "visitDate") Date visitDate
			) throws Exception {
		
		Diary diary = new Diary();
		diary.setDtitle(Dtitle);
		diary.setDcontent(Dcontent);
		diary.setLocation(Location);
		diary.setRating(rating);
		diary.setTheme(theme);
//		diary.setVisitDate(visitDate);
		
		if (image != null) {
			diaryService.attachimage(diary, image);
		}
		return diaryService.saveDiary(diary);
	}
	
	//visitdate
    // 날짜만 저장하는 POST 요청 처리
    @PostMapping("/saveVisitDate")
    public ResponseEntity<String> saveVisitDate(
            @RequestParam("seq") Long seq,
            @RequestParam("visitDate") Date visitDate
    ) {
        Diary diary = diaryService.getDiaryBySeq(seq);
        if (diary != null) {
            diary.setVisitDate(visitDate);
            diaryService.save(diary);
            return ResponseEntity.ok("방문 날짜가 성공적으로 저장되었습니다.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	// 이미지 경로 받아오기
	@GetMapping("/getDimage/{seq}")
	public ResponseEntity<String> getDimagePath(@PathVariable Long seq){
		Diary diary = diaryService.getDiaryBySeq(seq);
		if (diary != null) {
			String DimagePath = diary.getDimagepath();
			return ResponseEntity.ok(DimagePath);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	
	// 다이어리 목록
	@GetMapping("/getAllDiarys")
	public List<Diary> getAllDiarys(){
		return diaryService.getAllDiarys();
	}
	
	// 다이어리 검색
	@GetMapping("/searchDiary")
	public List<Diary> searchDiary(
			@RequestParam(name = "searchType") String searchType,
			@RequestParam(name = "keyword") String keyword) {
		return diaryService.searchDiary(searchType, keyword);
	}
	
	
	
	
	// 다이어리 삭제
	@Transactional
	@DeleteMapping("/deleteDiary/{seq}")
	public void deleteDiary(@PathVariable Long seq) {
		diaryService.deleteDiaryBySeq(seq);
	}
	
	// 다이어리 수정
//	@PutMapping("/updateDiary")
//	public Diary updateDiary(@RequestBody Diary diary) {
//		return diaryService.updateDiary(diary);
//	}
	@PutMapping("/updateDiary")
	public Diary updateDiary(
	    @RequestParam(name = "image", required = false) MultipartFile image,
	    @RequestParam(name = "seq") Long seq,
	    @RequestParam(name = "dtitle") String dtitle,
	    @RequestParam(name = "dcontent") String dcontent,
	    @RequestParam(name = "location") String location,
	    @RequestParam(name = "rating") String rating,
	    @RequestParam(name = "theme") String theme
	) throws Exception {
	    Diary diary = new Diary();
	    diary.setSeq(seq);
	    diary.setDtitle(dtitle);
	    diary.setDcontent(dcontent);
	    diary.setLocation(location);
	    diary.setRating(rating);
	    diary.setTheme(theme);

	    if (image != null) {
	        diaryService.attachImage(diary, image);
	    }

	    return diaryService.updateDiary(diary);
	}	
	
	
	
	@GetMapping("/getDiary/{seq}")
	public Diary getDiaryBySeq(@PathVariable Long seq) {
		return diaryService.getDiaryBySeq(seq);
	}
	
	
	
	
	
	
	
	// 다이어리 상세보기
	@GetMapping("/DiaryDetail")
	@ResponseBody
	public Map<String, Object> getDiaryDetail(@RequestParam Long seq){
		Map<String, Object> result = new HashMap<>();
		
		Diary diary1 = diaryService.getDiaryDetail(seq);
		result.put("diary1",diary1);
		return result;
	}
	
	

}































