package com.gdsc.petwalk.domain.photo.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CloudStorageServiceImpl implements CloudStorageService {

    private static final Logger log = LoggerFactory.getLogger(CloudStorageServiceImpl.class);
    private final Storage storage = StorageOptions.getDefaultInstance().getService();
    private final String bucketName;

    public CloudStorageServiceImpl(@Value("${cloud.gcp.storage.bucket}") String bucketName) {
        this.bucketName = bucketName;
        log.info("Cloud Storage 서비스가 {} 버킷으로 구성되었습니다.", bucketName);
    }

    @Override
    public String uploadFile(MultipartFile file, String fileName) {
        try {
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();
            storage.create(blobInfo, file.getBytes());
            String fileUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
            log.info("파일 '{}'을(를) '{}' 버킷에 성공적으로 업로드했습니다. 접근 URL: {}", fileName, bucketName, fileUrl);
            return fileUrl;
        } catch (StorageException e) {
            log.error("Google Cloud Storage에 파일 '{}'을(를) 업로드하는 동안 Storage 문제가 발생했습니다. 파일명: {}", fileName, e.getMessage());
            throw e;
        } catch (IOException e) {
            log.error("Google Cloud Storage에 파일 '{}'을(를) 업로드하는 동안 IO 문제가 발생했습니다. 파일명: {}", fileName, e.getMessage());
            throw new RuntimeException("Google Cloud Storage에 파일을 업로드하지 못했습니다.", e);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        BlobId blobId = BlobId.of(bucketName, fileName);
        boolean isDeleted = storage.delete(blobId);
        if (isDeleted) {
            log.info("파일 '{}'을(를) '{}' 버킷에서 성공적으로 삭제했습니다.", fileName, bucketName);
        } else {
            log.error("파일 '{}'을(를) '{}' 버킷에서 삭제하는데 실패했습니다.", fileName, bucketName);
            throw new RuntimeException("파일을 삭제하지 못했습니다.");
        }
    }
}
