package com.apartment.app.shared.port;

import org.springframework.web.multipart.MultipartFile;

public interface FileStoragePort {
    String uploadFile(MultipartFile file, String folder);
    void deleteFile(String url);
}
