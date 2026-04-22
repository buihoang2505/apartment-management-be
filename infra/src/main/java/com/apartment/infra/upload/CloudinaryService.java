package com.apartment.infra.upload;

import com.apartment.app.shared.port.FileStoragePort;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService implements FileStoragePort {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "auto"
                    ));
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            log.error("Cloudinary upload failed [folder={}]: {}", folder, e.getMessage(), e);
            throw new RuntimeException("Không thể upload file: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteFile(String url) {
        if (url == null || url.trim().isEmpty()) return;

        try {
            
            String publicId = extractPublicId(url);
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                log.info("Đã xóa file trên Cloudinary: {}", publicId);
            }
        } catch (Exception e) {
            log.error("Không thể xóa ảnh cũ trên Cloudinary: {}", url, e);
        }
    }

    private String extractPublicId(String url) {
        // Ví dụ URL: https://res.cloudinary.com/demo/image/upload/v1570540608/folder/filename.jpg
        // Chúng ta cần trích xuất "folder/filename"
        if (!url.contains("cloudinary.com")) return null;
        
        try {
            int uploadIndex = url.indexOf("/upload/");
            if (uploadIndex == -1) return null;
            
            String afterUpload = url.substring(uploadIndex + "/upload/".length());
            
            // Xóa phần version (v12345678/...) nếu có
            if (afterUpload.matches("^v\\d+/.*")) {
                afterUpload = afterUpload.substring(afterUpload.indexOf('/') + 1);
            }
            
            // Xóa phần mở rộng (ví dụ .jpg, .png)
            int lastDot = afterUpload.lastIndexOf('.');
            if (lastDot > -1) {
                return afterUpload.substring(0, lastDot);
            }
            return afterUpload;
        } catch (Exception e) {
            log.warn("Không thể trích xuất public ID từ: {}", url);
            return null;
        }
    }
}
