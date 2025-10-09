package sg.nusiss.t6.caproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import sg.nusiss.t6.caproject.model.Product;
import sg.nusiss.t6.caproject.model.Review;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    // --- 用户侧功能 ---
    List<Product> getAllVisibleProducts();

    Page<Product> getAllVisibleProducts(Pageable pageable);

    Optional<Product> getProductById(Integer id);

    List<Review> getReviewsByProductId(Integer productId);

    @Transactional(readOnly = true)
    List<Review> getReviewsByProductId(Long productId);

    Review addReviewToProduct(Integer productId, Review review);

    // --- 管理员侧功能 ---
    List<Product> getAllProducts();

    Page<Product> getAllProducts(Pageable pageable);

    Product createProduct(Product product);

    Optional<Product> updateProduct(Integer id, Product productDetails);

    void deleteProduct(Integer id);

    Optional<Product> updateStock(Integer id, Integer newStock);

    Optional<Product> setProductVisibility(Integer id, boolean isVisible);
}