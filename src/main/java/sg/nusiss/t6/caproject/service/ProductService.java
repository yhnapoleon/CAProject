package sg.nusiss.t6.caproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sg.nusiss.t6.caproject.model.Product;
import sg.nusiss.t6.caproject.model.Review;
import sg.nusiss.t6.caproject.controller.dto.ReviewRequestDTO;
import sg.nusiss.t6.caproject.controller.dto.ReviewResponseDTO;
import sg.nusiss.t6.caproject.controller.dto.ProductRequestDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    // --- 用户侧功能 ---
    List<Product> getAllVisibleProducts();

    Page<Product> getAllVisibleProducts(Pageable pageable);

    Optional<Product> getProductById(Integer id);

    List<Review> getReviewsByProductId(Integer productId);
    
    List<ReviewResponseDTO> getReviewsWithUserNameByProductId(Integer productId);

    Review addReviewToProduct(Integer productId, ReviewRequestDTO reviewRequest);

    // 测试用：无鉴权写入评论
    Review addReviewToProductForTest(Integer productId, ReviewRequestDTO reviewRequest);

    // --- 管理员侧功能 ---
    List<Product> getAllProducts();

    Page<Product> getAllProducts(Pageable pageable);

    Product createProduct(ProductRequestDTO product);

    Optional<Product> updateProduct(Integer id, ProductRequestDTO productDetails);

    void deleteProduct(Integer id);

    Optional<Product> updateStock(Integer id, Integer newStock);

    Optional<Product> setProductVisibility(Integer id, boolean isVisible);

    /** 更新商品图片：根据商品ID存储图片并更新数据库路径，返回更新后的商品 */
    Optional<Product> updateProductImage(Integer id, String absolutePath);

    /**
     * Handles storing a new image file for an existing product and updating its
     * record.
     *
     * @param id   The ID of the product to update.
     * @param file The new image file uploaded by the user.
     * @return An Optional containing the updated Product if successful.
     */
    Optional<Product> updateProductImage(Integer id, MultipartFile file);
}