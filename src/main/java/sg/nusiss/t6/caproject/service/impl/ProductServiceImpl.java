package sg.nusiss.t6.caproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nusiss.t6.caproject.model.Product;
import sg.nusiss.t6.caproject.model.Review;
import sg.nusiss.t6.caproject.repository.ProductRepository;
import sg.nusiss.t6.caproject.repository.ReviewRepository;
import sg.nusiss.t6.caproject.service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    // --- 用户侧功能实现 ---

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllVisibleProducts() {
        // 默认返回全部可见商品；如需分页可在 Controller 上层处理或拓展
        return productRepository.findByIsVisible(1, org.springframework.data.domain.Pageable.unpaged()).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getReviewsByProductId(Integer productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    @Transactional
    public Review addReviewToProduct(Integer productId, Review review) {
        // 查找商品，如果不存在则抛出异常
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        review.setProduct(product);
        // 在实际应用中，User 对象应该从 SecurityContext 中获取
        // review.setUser(currentUser);
        return reviewRepository.save(review);
    }

    // --- 管理员侧功能实现 ---

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProduct(Integer id, Product productDetails) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        existingProduct.setProductName(productDetails.getProductName());
        existingProduct.setProductCategory(productDetails.getProductCategory());
        existingProduct.setProductDescription(productDetails.getProductDescription());
        existingProduct.setProductPrice(productDetails.getProductPrice());
        existingProduct.setProductStockQuantity(productDetails.getProductStockQuantity());
        existingProduct.setImageUrl(productDetails.getImageUrl());
        existingProduct.setIsVisible(productDetails.getIsVisible());
        return productRepository.save(existingProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Product updateStock(Integer id, Integer newStock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        product.setProductStockQuantity(newStock);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product setProductVisibility(Integer id, boolean isVisible) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        product.setIsVisible(isVisible ? 1 : 0);
        return productRepository.save(product);
    }
}