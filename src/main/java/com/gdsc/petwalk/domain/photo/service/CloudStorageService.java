package com.gdsc.petwalk.domain.photo.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudStorageService {
    String uploadFile(MultipartFile file, String fileName);
}
