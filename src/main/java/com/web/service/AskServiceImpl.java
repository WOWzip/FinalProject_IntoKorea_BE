

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
import com.web.persistence.AskRepository;

@Service
public class AskServiceImpl implements AskService {


	@Autowired
	private AskRepository askRepo;

	// Q&A 작성
    @Override
    public Ask saveAsk(Ask ask){
    	

        return askRepo.save(ask);
    }
    
    // 첨부파일
    @Override
    public void attachfile(Ask ask, MultipartFile file) throws Exception {
    	
    	String projectPath = "C://Users//LJW//ikfront//Front-End//src//files";
    	
    	UUID uuid = UUID.randomUUID();
    	
    	String fileName = uuid + "_" + file.getOriginalFilename();
    	
    	File saveFile = new File(projectPath, fileName);
    	
    	file.transferTo(saveFile);
    	
    	ask.setFilename(fileName);
    	ask.setFilepath("/files/"+fileName);
    	
    	askRepo.save(ask);
    	
    }
    
//    // 첨부파일 다운
//    @Override
//    public byte[] selectFile(Ask ask) {
//        Path filePath = Paths.get("C:/Users/LJW/ikfront/Front-End/src/files", ask.getFilename());
//        
//        try {
//            return Files.readAllBytes(filePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
	
	// Q&A 목록
	@Override
	 public List<Ask> getAllAsks(){
		return (List<Ask>) askRepo.findAll();
	}
	
	// Q&A 삭제
    @Override
    public void deleteAskBySeq(Long seq) {
        askRepo.deleteBySeq(seq);
    }
    
    // Q&A 수정
    @Override
    public Ask updateAsk(Ask ask) {
        Long seq = ask.getSeq();
        Ask existingAsk = askRepo.findBySeq(seq);

        if (existingAsk != null) {
            existingAsk.setTitle(ask.getTitle());
            existingAsk.setContent(ask.getContent());
            return askRepo.save(existingAsk);
        } else {
            return null;
        }
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
    
    
    
    
    

}
