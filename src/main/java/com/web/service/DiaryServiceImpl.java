package com.web.service;

import java.io.File;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.domain.Diary;
import com.web.persistence.DiaryRepository;

@Service
public class DiaryServiceImpl implements DiaryService {
   
   @Autowired
   private DiaryRepository diaryRepo;
   
   // 다이어리 작성
   @Override
   public Diary save(Diary diary) {
      return diaryRepo.save(diary);
   }
   
   // 다이어리 작성 + 이미지
   @Override
   public Diary saveDiary(Diary diary) {
      
      return diaryRepo.save(diary);
   }
   @Override
   public void attachimage(Diary diary, MultipartFile image) throws Exception {
      String projectPath = "D://_FinalProject//WorkFront//intokorea//public//picture";
      
      UUID uuid = UUID.randomUUID();
      
      String imageName = uuid + "_" + image.getOriginalFilename();
      
      File saveImage = new File(projectPath, imageName);
      
      image.transferTo(saveImage);
      
      diary.setDimage(imageName);
      diary.setDimagepath("/picture/"+imageName);
      
      diaryRepo.save(diary);
   }
   
   // visitdate
    @Override
    public Diary saveVisitDate(Long seq, Date visitDate) {
        Diary diary = diaryRepo.findById(seq).orElse(null);
        if (diary != null) {
            diary.setVisitDate(visitDate);
            diaryRepo.save(diary);
            return diary;
        } else {
            return null; // 혹은 예외 처리를 수행할 수 있습니다.
        }
    }
   
   
   
   // 다이어리 목록
   @Override
   public List<Diary> getAllDiarys(){
      return (List<Diary>) diaryRepo.findAll();
   }

   // 다이어리 검색
    @Override
    public List<Diary> searchDiary(String searchType, String keyword) {
        switch (searchType) {
            case "title":
                return diaryRepo.findByDtitleContaining(keyword);
            case "theme":
                return diaryRepo.findByThemeContaining(keyword);
            case "content":
                return diaryRepo.findByDcontentContaining(keyword);
            default:
                return Collections.emptyList();
        }
    }
   
   
   // 다이어리 삭제
   @Override
   public void deleteDiaryBySeq(Long seq) {
      diaryRepo.deleteBySeq(seq);
   }
   
   // 다이어리 수정
   @Override
   public Diary updateDiary(Diary diary) {
      Long seq = diary.getSeq();
      Diary existingDiary = diaryRepo.findBySeq(seq);
      
      if (existingDiary != null) {
         existingDiary.setDtitle(diary.getDtitle());
         existingDiary.setDcontent(diary.getDcontent());
         existingDiary.setLocation(diary.getLocation());
         existingDiary.setRating(diary.getRating());
         return diaryRepo.save(existingDiary);
      } else {
         return null;
      }
      
   }
    @Override
    public Diary attachImage(Diary diary, MultipartFile image) throws Exception {
        if (image != null && !image.isEmpty()) {
            String projectPath = "D:/_FinalProject/WorkFront/intokorea/public/picture";
            UUID uuid = UUID.randomUUID();
            String imageName = uuid + "_" + image.getOriginalFilename();
            File saveImage = new File(projectPath, imageName);
            image.transferTo(saveImage);
            
            diary.setDimage(imageName);
            diary.setDimagepath("/picture/" + imageName);
        }
        return diaryRepo.save(diary);
    }   
   
   
   
   @Override
   public Diary getDiaryBySeq(Long seq) {
      return diaryRepo.findBySeq(seq);
   }
   
   // 다이어리 상세보기
   @Override
   public Diary getDiaryDetail(Long seq) {
      return diaryRepo.findById(seq).orElse(null);
   }
   
   
}


















