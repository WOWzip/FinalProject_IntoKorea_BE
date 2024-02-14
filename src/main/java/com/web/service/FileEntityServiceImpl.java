package com.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.domain.FileEntity;
import com.web.persistence.FileEntityRepository;

@Service
public class FileEntityServiceImpl implements FileEntityService {

    @Autowired
    private FileEntityRepository fileEntityRepository;

    @Override
    public FileEntity storeFile(MultipartFile file) {
        try {
            // 파일 정보 저장
            String fileName = file.getOriginalFilename();
            String filePath = "/path/to/upload/directory/" + fileName; // 실제 파일이 저장될 경로

            // 파일을 디렉토리에 저장
            // (파일 저장 로직은 실제 환경에 맞게 수정 필요)
            // 예를 들어, Files.write() 등을 사용하여 저장 가능
            // 여기서는 예시로 그냥 파일 정보만 저장
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(fileName);
            fileEntity.setFilePath(filePath);

            return fileEntityRepository.save(fileEntity);
        } catch (Exception e) {
            throw new RuntimeException("파일 저장에 실패하였습니다.", e);
        }
    }
    
    @Override
    public void saveFile(FileEntity fileEntity) {
        fileEntityRepository.save(fileEntity);
    }
}











