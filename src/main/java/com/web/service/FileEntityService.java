package com.web.service;

import org.springframework.web.multipart.MultipartFile;

import com.web.domain.FileEntity;

public interface FileEntityService {
    FileEntity storeFile(MultipartFile file);
    void saveFile(FileEntity fileEntity);
}

