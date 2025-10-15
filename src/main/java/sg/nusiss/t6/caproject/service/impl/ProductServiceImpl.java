package sg.nusiss.t6.caproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import sg.nusiss.t6.caproject.model.Product;
import sg.nusiss.t6.caproject.model.Review;
import sg.nusiss.t6.caproject.model.User;
import sg.nusiss.t6.caproject.controller.dto.ReviewRequestDTO;
import sg.nusiss.t6.caproject.controller.dto.ProductRequestDTO;
import sg.nusiss.t6.caproject.repository.ProductRepository;
import sg.nusiss.t6.caproject.repository.ReviewRepository;
import sg.nusiss.t6.caproject.repository.UserRepository;
import sg.nusiss.t6.caproject.service.ProductService;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ReviewRepository reviewRepository,
            UserRepository userRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    // --- 用户侧功能实现 ---

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllVisibleProducts() {
        return productRepository.findByIsVisible(1);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getAllVisibleProducts(Pageable pageable) {
        return productRepository.findByIsVisible(1, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getReviewsByProductId(Integer productId) {
        return reviewRepository.findByProductProductId(productId.longValue());
    }

    @Override
    @Transactional
    public Review addReviewToProduct(Integer productId, ReviewRequestDTO reviewRequest) {
        // 查找商品，如果不存在则抛出异常
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        Review review = new Review();
        review.setProduct(product);
        review.setComment(reviewRequest.getComment());
        review.setReviewRank(reviewRequest.getReviewRank());
        // 从安全上下文中获取当前用户并关联
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        review.setUser(user);
        return reviewRepository.save(review);
    }

    @Override
    @Transactional
    public Review addReviewToProductForTest(Integer productId, ReviewRequestDTO reviewRequest) {
        // 查找商品，如果不存在则抛出异常
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        // 使用硬编码测试用户ID
        User user = userRepository.findById(100031)
                .orElseThrow(() -> new RuntimeException("Test user not found: 100031"));

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setComment(reviewRequest.getComment());
        review.setReviewRank(reviewRequest.getReviewRank());
        return reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // --- 管理员侧功能实现 ---

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Product createProduct(ProductRequestDTO product) {
        // 从安全上下文确定当前用户作为商品的 owner/admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Product entity = new Product();
        entity.setProductName(product.getProductName());
        entity.setProductCategory(product.getProductCategory());
        entity.setProductDescription(product.getProductDescription());
        entity.setProductPrice(product.getProductPrice());
        entity.setProductStockQuantity(product.getProductStockQuantity());
        entity.setImageUrl(product.getImageUrl());
        entity.setIsVisible(product.getIsVisible());
        entity.setUser(user);
        return productRepository.save(entity);
    }

    @Override
    @Transactional
    public Optional<Product> updateProduct(Integer id, ProductRequestDTO productDetails) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setProductName(productDetails.getProductName());
            existingProduct.setProductCategory(productDetails.getProductCategory());
            existingProduct.setProductDescription(productDetails.getProductDescription());
            existingProduct.setProductPrice(productDetails.getProductPrice());
            existingProduct.setProductStockQuantity(productDetails.getProductStockQuantity());
            existingProduct.setImageUrl(productDetails.getImageUrl());
            existingProduct.setIsVisible(productDetails.getIsVisible());
            return productRepository.save(existingProduct);
        });
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Product> updateStock(Integer id, Integer newStock) {
        return productRepository.findById(id).map(product -> {
            product.setProductStockQuantity(newStock);
            return productRepository.save(product);
        });
    }

    @Override
    @Transactional
    public Optional<Product> setProductVisibility(Integer id, boolean isVisible) {
        return productRepository.findById(id).map(product -> {
            product.setIsVisible(isVisible ? 1 : 0);
            return productRepository.save(product);
        });
    }

    @Override
    @Transactional
    public Optional<Product> updateProductImage(Integer id, String absolutePath) {
        return productRepository.findById(id).map(product -> {
            product.setImageUrl(absolutePath);
            return productRepository.save(product);
        });
    }
}