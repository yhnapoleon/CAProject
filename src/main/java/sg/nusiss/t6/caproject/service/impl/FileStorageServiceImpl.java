//By Ying Hao

package sg.nusiss.t6.caproject.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import sg.nusiss.t6.caproject.service.FileStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    // Keep consistent with static mapping: use app.image-path (e.g., E:/images/) as
    // the physical storage directory
    @Value("${app.image-path}")
    private String uploadDir;

    @Override
    public String storeProductImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int idx = originalFilename.lastIndexOf('.');
        if (idx >= 0) {
            ext = originalFilename.substring(idx);
        }
        String newFilename = UUID.randomUUID().toString().replace("-", "") + ext;

        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);
            Path target = dir.resolve(newFilename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            // Return the local absolute path on the backend device for DB storage
            return target.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }

    @Override
    public String storeProductImageWithName(MultipartFile file, String filename) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }
        String clean = StringUtils.cleanPath(filename);
        try {
            Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(dir);
            Path target = dir.resolve(clean);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return target.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }
}