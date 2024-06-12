package com.gdsc.petwalk.domain.photo.service;

import com.gdsc.petwalk.domain.pet.entity.Pet;
import com.gdsc.petwalk.domain.photo.entity.Photo;
import com.gdsc.petwalk.domain.photo.repository.PhotoRepository;
import com.gdsc.petwalk.domain.walkinvitation.entity.WalkInvitation;
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

    public List<Photo> savePhotosToWalkInvitation(MultipartFile[] multipartFiles, WalkInvitation walkInvitation) {
        List<Photo> savedPhotos = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            String url = uploadAndGetUrl(file);
            Photo photo = Photo.builder()
                .photoUrl(url)
                .walkInvitation(walkInvitation)
                .build();

            savedPhotos.add(photoRepository.save(photo));
        }

        return savedPhotos;
    }

    public List<Photo> savePhotoToPet(MultipartFile file, Pet pet){
        List<Photo> savedPhotos = new ArrayList<>();

            String url = uploadAndGetUrl(file);
            Photo photo = Photo.builder()
                    .photoUrl(url)
                    .pet(pet)
                    .build();
            savedPhotos.add(photoRepository.save(photo));

        return savedPhotos;
    }

    public List<String> getPhotosByWalkInvitationId(Long walkInvitationId) {
        List<Photo> photos = photoRepository.findByWalkInvitationId(walkInvitationId);
        return photos.stream()
            .map(Photo::getPhotoUrl)
            .toList();
    }

    public void updatePhoto(Long photoId, MultipartFile newFile) {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new RuntimeException("Photo not found"));
        String oldUrl = photo.getPhotoUrl();
        String newUrl = uploadAndGetUrl(newFile);
        cloudStorageService.deleteFile(oldUrl);
        photo.update(newUrl);
        photoRepository.save(photo);
    }

    public void deletePhoto(Long photoId) {
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new RuntimeException("Photo not found"));
        cloudStorageService.deleteFile(photo.getPhotoUrl());
        photoRepository.deleteById(photoId);
    }

    public String uploadAndGetUrl(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
        return cloudStorageService.uploadFile(file, uniqueFilename);
    }
}
