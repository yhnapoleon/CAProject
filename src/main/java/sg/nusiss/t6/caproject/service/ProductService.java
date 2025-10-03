package sg.nusiss.t6.caproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sg.nusiss.t6.caproject.model.Product;
import sg.nusiss.t6.caproject.model.Review;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    // --- 用户侧功能 ---
    Page<Product> getAllVisibleProducts(Pageable pageable);
    Optional<Product> getProductById(Long id);
    List<Review> getReviewsByProductId(Long productId);
    Review addReviewToProduct(Long productId, Review review);

    // --- 管理员侧功能 ---
    Page<Product> getAllProducts(Pageable pageable);
    Product createProduct(Product product);
    Optional<Product> updateProduct(Long id, Product productDetails);
    void deleteProduct(Long id);
    Optional<Product> updateStock(Long id, Integer newStock);
    Optional<Product> setProductVisibility(Long id, boolean isVisible);
}