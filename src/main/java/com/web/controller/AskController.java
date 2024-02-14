
package com.web.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.web.domain.Ask;
import com.web.service.AskService;
@RestController
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RequestMapping("/mypage")
public class AskController {

	@Autowired
	private AskService askService;

	// Q&A 작성
	@Transactional
	@PostMapping(value = "/submitQuestion", consumes = "multipart/form-data")
	public Ask submitQuestion(@RequestPart(name = "file", required = false) MultipartFile file,
			@RequestPart(name = "title") String title, @RequestPart(name = "content") String content) throws Exception {
		Ask ask = new Ask();
		ask.setTitle(title);
		ask.setContent(content);

		if (file != null) {
			askService.attachfile(ask, file);
		}
		return askService.saveAsk(ask);
	}
	
	
	
//	// 첨부파일 다운로드
//	@Transactional
//	@GetMapping("/download/{seq}")
//	public ResponseEntity<Resource> downloadFile(@PathVariable Long seq, HttpServletResponse response) {
//	    Ask ask = askService.getAskBySeq(seq);
//
//	    if (ask == null || ask.getFilename() == null || ask.getFilepath() == null) {
//	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//	    }
//
//	    // 파일경로
//	    Path filePath = Paths.get("C:/Users/LJW/ikfront/Front-End/src/files", ask.getFilename());
//	    Resource resource;
//
//	    try {
//	        resource = new UrlResource(filePath.toUri());
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//	    }
//
//	    // 다운로드 파일 이름 설정
//	    String contentType;
//	    try {
//	        contentType = Files.probeContentType(filePath);
//	    } catch (IOException e) {
//	        e.printStackTrace();
//	        contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
//	    }
//
//	    // 다운로드 헤더 설정
//	    return ResponseEntity.ok()
//	            .contentType(MediaType.parseMediaType(contentType))
//	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + ask.getFilename() + "\"")
//	            .body(resource);
//	}
	 
//	//첨부파일 다운로드
//	@GetMapping(value = "C:/Users/LJW/ikfront/Front-End/src/files")
//	public ResponseEntity<ByteArrayResource> downloadFile(Ask ask){
//		
//		byte[] fileData = askService.selectFile(ask);
//		
//		if (fileData != null) {
//			ByteArrayResource resource = new ByteArrayResource(fileData);
//			
//			return ResponseEntity.ok()
//					.contentType(MediaType.APPLICATION_OCTET_STREAM)
//					.contentLength(fileData.length)
//					.body(resource);
//		} else {
//			return ResponseEntity.notFound().build();
//		}
//	}
	
	// 파일 다운로드
	@Transactional
	@GetMapping("/download/{seq}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long seq, HttpServletResponse response) {
	    Ask ask = askService.getAskBySeq(seq);

	    if (ask == null || ask.getFilename() == null || ask.getFilepath() == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    // 파일경로
	    Path filePath = Paths.get("C:/Users/LJW/ikfront/Front-End/src/files", ask.getFilename());
	    Resource resource;

	    try {
	        resource = new UrlResource(filePath.toUri());
	    } catch (IOException e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    // 다운로드 파일 이름 설정
	    String contentType;
	    try {
	        contentType = Files.probeContentType(filePath);
	    } catch (IOException e) {
	        e.printStackTrace();
	        contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
	    }

	    // 다운로드 헤더 설정
	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + ask.getFilename() + "\"")
	            .body(resource);
	}	

	// Q&A 목록
	@GetMapping("/getAllAsks")
	public List<Ask> getAllAsks() {
		return askService.getAllAsks();
	}

	// Q&A 삭제
	@Transactional
	@DeleteMapping("/deleteAsk/{seq}")
	public void deleteAsk(@PathVariable Long seq) {
		askService.deleteAskBySeq(seq);
	}

	// Q&A 수정
	@PutMapping("/updateAsk")
	public Ask updateAsk(@RequestBody Ask ask) {
		System.out.println(ask);
		return askService.updateAsk(ask);
	}

	@GetMapping("/getAsk/{seq}")
	public Ask getAskBySeq(@PathVariable Long seq) {
		System.out.println(seq);
		return askService.getAskBySeq(seq);
	}

	// Q&A 상세보기
	@GetMapping("/detail")
	@ResponseBody
	public Map<String, Object> getDetail(@RequestParam Long seq) {
		Map<String, Object> result = new HashMap<>();

		Ask ask1 = askService.getDetail(seq);
		result.put("ask1", ask1);
		return result;
	}

}



