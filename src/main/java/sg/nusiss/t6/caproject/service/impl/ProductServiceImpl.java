package sg.nusiss.t6.caproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nusiss.t6.caproject.model.Product;
import sg.nusiss.t6.caproject.model.Review;
import sg.nusiss.t6.caproject.repository.ProductRepository;
import sg.nusiss.t6.caproject.repository.ReviewRepository;
import sg.nusiss.t6.caproject.service.ProductService;

import java.util.List;
import java.util.Optional;

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
    public Page<Product> getAllVisibleProducts(Pageable pageable) {
        return productRepository.findByIsVisible(1, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    @Transactional
    public Review addReviewToProduct(Long productId, Review review) {
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
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Optional<Product> updateProduct(Long id, Product productDetails) {
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
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Product> updateStock(Long id, Integer newStock) {
        return productRepository.findById(id).map(product -> {
            product.setProductStockQuantity(newStock);
            return productRepository.save(product);
        });
    }

    @Override
    @Transactional
    public Optional<Product> setProductVisibility(Long id, boolean isVisible) {
        return productRepository.findById(id).map(product -> {
            product.setIsVisible(isVisible ? 1 : 0);
            return productRepository.save(product);
        });
    }
}