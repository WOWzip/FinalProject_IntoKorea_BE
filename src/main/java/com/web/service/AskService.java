package com.web.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.domain.Ask;

@Service
public interface AskService {

	// Q&A 작성
    public Ask saveAsk(Ask ask);	
    
    // 첨부파일
    public void attachfile(Ask ask, MultipartFile file) throws Exception;
//    // 첨부파일 다운
//    byte[] selectFile(Ask ask);
    
    // Q&A 목록
//    public List<Ask> getAllAsks();
    List<Ask> getAllAsksInDescendingOrder();
    
    // Q&A 삭제
    public void deleteAskBySeq(Long seq);
    
    // Q&A 수정
    public Ask updateAsk(Ask ask);
    public Ask getAskBySeq(Long seq);
    Ask attachFile(Ask ask, MultipartFile file) throws Exception;
    
    // Q&A 디테일
    public Ask getDetail(Long seq);
    
//    List<Ask> getAllAsksForCurrentUser(String userEmail);
	
}
