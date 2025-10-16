//By Ying Hao and Zhao Jiayi

package sg.nusiss.t6.caproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import sg.nusiss.t6.caproject.model.Product;
import sg.nusiss.t6.caproject.model.Review;
import sg.nusiss.t6.caproject.model.User;
import sg.nusiss.t6.caproject.controller.dto.ReviewRequestDTO;
import sg.nusiss.t6.caproject.controller.dto.ReviewResponseDTO;
import sg.nusiss.t6.caproject.controller.dto.ProductRequestDTO;
import sg.nusiss.t6.caproject.repository.ProductRepository;
import sg.nusiss.t6.caproject.repository.ReviewRepository;
import sg.nusiss.t6.caproject.repository.UserRepository;
import sg.nusiss.t6.caproject.service.ProductService;
import sg.nusiss.t6.caproject.service.FileStorageService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ReviewRepository reviewRepository,
            UserRepository userRepository, FileStorageService fileStorageService) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    // --- Customer-facing feature implementations ---

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
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getReviewsWithUserNameByProductId(Integer productId) {
        // Use JOIN FETCH to preload User info to avoid LazyInitializationException
        List<Review> reviews = reviewRepository.findByProductProductIdWithUser(productId.longValue());
        return reviews.stream()
                .map(review -> {
                    String userName = review.getUser() != null ? review.getUser().getUserName() : "Unknown User";
                    return new ReviewResponseDTO(
                            review.getReviewId(),
                            review.getTitle(),
                            review.getComment(),
                            review.getReviewCreateTime(),
                            review.getReviewRank(),
                            userName);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Review addReviewToProduct(Integer productId, ReviewRequestDTO reviewRequest) {
        // Find product; throw if not found
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        Review review = new Review();
        review.setProduct(product);
        review.setTitle(reviewRequest.getTitle());
        review.setComment(reviewRequest.getComment());
        review.setReviewRank(reviewRequest.getReviewRank());
        // Get current user from security context and associate it
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
        // Find product; throw if not found
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        // Use hard-coded test user ID
        User user = userRepository.findById(100031)
                .orElseThrow(() -> new RuntimeException("Test user not found: 100031"));

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setTitle(reviewRequest.getTitle());
        review.setComment(reviewRequest.getComment());
        review.setReviewRank(reviewRequest.getReviewRank());
        return reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // --- Admin-facing feature implementations ---

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Product createProduct(ProductRequestDTO product) {
        // Determine current user from security context as the product owner/admin
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

    /**
     * Save a new image and update the database record: use product ID as the
     * filename (keep extension) and overwrite old image.
     */
    @Override
    @Transactional
    public Optional<Product> updateProductImage(Integer id, MultipartFile file) {
        return productRepository.findById(id).map(product -> {
            String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "";
            String ext = "";
            int idx = original.lastIndexOf('.');
            if (idx >= 0) {
                ext = original.substring(idx);
            }
            String filename = id + ext;
            fileStorageService.storeProductImageWithName(file, filename);
            String publicUrl = "/images/" + filename;
            product.setImageUrl(publicUrl);
            return productRepository.save(product);
        });
    }
}