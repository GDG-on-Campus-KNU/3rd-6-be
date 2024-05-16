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
            log.info("파일을 성공적으로 업로드했습니다.: {}", fileUrl);
            return fileUrl;
        } catch (StorageException e) {
            log.error("Storage 이슈로 인해 Google Cloud Storage에 파일을 업로드하지 못 했습니다.", e);
            throw e;
        } catch (IOException e) {
            log.error("IO 이슈로 인해 Google Cloud Storage에 파일을 업로드하지 못 했습니다.", e);
            throw new RuntimeException("Google Cloud Storage에 파일을 업로드하지 못 했습니다.", e);
        }
    }
}
