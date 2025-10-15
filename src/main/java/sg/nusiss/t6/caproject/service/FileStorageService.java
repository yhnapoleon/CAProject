package sg.nusiss.t6.caproject.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /**
     * 保存产品图片到本地文件系统并返回可公开访问的 URL（例如 /uploads/xxx.png）。
     */
    String storeProductImage(MultipartFile file);
}


