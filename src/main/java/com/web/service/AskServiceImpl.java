

package com.web.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.domain.Ask;
import com.web.domain.Member;
import com.web.persistence.AskRepository;

@Service
public class AskServiceImpl implements AskService {


   @Autowired
   private AskRepository askRepo;
   
    @Autowired
    private MemberService memberService;

   // Q&A 작성
    @Override
    public Ask saveAsk(Ask ask){
       

        return askRepo.save(ask);
    }
    
    // 첨부파일
    @Override
    public void attachfile(Ask ask, MultipartFile file) throws Exception {
       
       String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";

       UUID uuid = UUID.randomUUID();
       
       String fileName = uuid + "_" + file.getOriginalFilename();
       
       File saveFile = new File(projectPath, fileName);
       
       file.transferTo(saveFile);
       
       ask.setFilename(fileName);
       ask.setFilepath("/files/"+fileName);
       
       askRepo.save(ask);
       
    }
    
   
   // Q&A 목록
//   @Override
//    public List<Ask> getAllAsks(){
//      return (List<Ask>) askRepo.findAll();
//   }
    @Override
    public List<Ask> getAllAsksInDescendingOrder() {
        return askRepo.findAllByOrderBySeqDesc();
    }
   // Q&A 삭제
    @Override
    public void deleteAskBySeq(Long seq) {
        askRepo.deleteBySeq(seq);
    }
    
//    // Q&A 수정
//    @Override
//    public Ask updateAsk(Ask ask) {
//        return askRepo.save(ask);
//    }
    
    // 수정
    @Override
    public Ask updateAsk(Ask ask) {
       Long seq = ask.getSeq();
       Ask exAsk = askRepo.findBySeq(seq);
       
       if (exAsk != null) {
          exAsk.setTitle(ask.getTitle());
          exAsk.setContent(ask.getContent());
          return askRepo.save(exAsk);
       } else {
          return null;
       }
    }
    @Override
    public Ask attachFile(Ask ask, MultipartFile file) throws Exception {
       if (file != null && !file.isEmpty()) {
           String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
//                 "C://Users//LJW//ikfront//Front-End//src//files";

           UUID uuid = UUID.randomUUID();
           
           String fileName = uuid + "_" + file.getOriginalFilename();
           
           File saveFile = new File(projectPath, fileName);
           
           file.transferTo(saveFile);
           
           ask.setFilename(fileName);
           ask.setFilepath("/files/"+fileName);
       }
       return askRepo.save(ask);
    }
    
    
    @Override
    public Ask getAskBySeq(Long seq) {
       return askRepo.findBySeq(seq);
    }
    
    // Q&A 디테일
    @Override
    public Ask getDetail(Long seq) {
       return askRepo.findById(seq).orElse(null);
    }
    
//    @Override
//    public List<Ask> getAllAsksForCurrentUser(String userEmail) {
//        Member member = memberService.findMemberByEmail(userEmail);
//        return askRepo.findByMember(member);
//    }
    
    
    

}