package sg.nusiss.t6.caproject.service;

import sg.nusiss.t6.caproject.model.Product;
import sg.nusiss.t6.caproject.model.Review;

import java.util.List;

public interface ProductService {

    // --- 用户侧功能 ---
    List<Product> getAllVisibleProducts();

    Product getProductById(Integer id);

    List<Review> getReviewsByProductId(Integer productId);

    Review addReviewToProduct(Integer productId, Review review);

    // --- 管理员侧功能 ---
    List<Product> getAllProducts();

    Product createProduct(Product product);

    Product updateProduct(Integer id, Product productDetails);

    void deleteProduct(Integer id);

    Product updateStock(Integer id, Integer newStock);

    Product setProductVisibility(Integer id, boolean isVisible);
}