package com.gdsc.petwalk.domain.photo.service;

import com.gdsc.petwalk.domain.photo.entity.Photo;
import com.gdsc.petwalk.domain.photo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final CloudStorageService cloudStorageService;

    public List<String> getPhotoUrls(MultipartFile[] multipartFiles) {
        List<String> photos = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            String originalFilename = file.getOriginalFilename();
            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
            String url = cloudStorageService.uploadFile(file, uniqueFilename);

            photos.add(url);
        }

        return photos;
    }

    public List<Photo> savePhotos(MultipartFile[] multipartFiles) {
        List<Photo> savedPhotos = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            String originalFilename = file.getOriginalFilename();
            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
            String url = cloudStorageService.uploadFile(file, uniqueFilename);
            Photo photo = Photo.builder()
                .photoUrl(url)
                .build();

            savedPhotos.add(photoRepository.save(photo));
        }

        return savedPhotos;
    }
}
