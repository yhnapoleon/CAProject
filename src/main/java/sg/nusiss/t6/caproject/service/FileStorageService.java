//By Ying Hao
//This feature has not been implemented yet.

package sg.nusiss.t6.caproject.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /**
     * Save product image to local filesystem and return a publicly accessible URL
     * (e.g., /uploads/xxx.png).
     */
    String storeProductImage(MultipartFile file);

    /**
     * Save image with a specified filename (e.g., 12345.jpg) and return the local
     * absolute path.
     */
    String storeProductImageWithName(MultipartFile file, String filename);
}
