package com.gdsc.petwalk.domain.photo.controller;

import com.gdsc.petwalk.domain.photo.entity.Photo;
import com.gdsc.petwalk.domain.photo.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<String>> uploadPhotos(@RequestPart("photos") MultipartFile[] files) {
        try {
            List<String> photoUrls = photoService.savePhotos(files).stream()
                .map(Photo::getPhotoUrl)
                .toList();
            return ResponseEntity.ok(photoUrls);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사진을 업로드할 수 없습니다.", e);
        }
    }

    @GetMapping("/walkInvitation/{walkInvitationId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<String>> getPhotosByWalkInvitation(@PathVariable Long walkInvitationId) {
        List<String> photoUrls = photoService.getPhotosByWalkInvitationId(walkInvitationId);
        return ResponseEntity.ok(photoUrls);
    }

    @PutMapping("/{photoId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updatePhoto(@PathVariable Long photoId, @RequestPart("photo") MultipartFile file) {
        try {
            photoService.updatePhoto(photoId, file);
            return ResponseEntity.ok("사진이 업데이트 되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사진을 업데이트하지 못했습니다.");
        }
    }

    @DeleteMapping("/{photoId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long photoId) {
        try {
            photoService.deletePhoto(photoId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
