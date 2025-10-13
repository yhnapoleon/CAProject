package sg.nusiss.t6.caproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sg.nusiss.t6.caproject.model.Product;
import sg.nusiss.t6.caproject.model.Review;
import sg.nusiss.t6.caproject.controller.dto.ReviewRequestDTO;
import sg.nusiss.t6.caproject.controller.dto.ProductRequestDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    // --- 用户侧功能 ---
    List<Product> getAllVisibleProducts();

    Page<Product> getAllVisibleProducts(Pageable pageable);

    Optional<Product> getProductById(Integer id);

    List<Review> getReviewsByProductId(Integer productId);

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
}