//By Ying Hao

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

    // --- Customer-facing features ---
    List<Product> getAllVisibleProducts();

    Page<Product> getAllVisibleProducts(Pageable pageable);

    Optional<Product> getProductById(Integer id);

    List<Review> getReviewsByProductId(Integer productId);

    List<ReviewResponseDTO> getReviewsWithUserNameByProductId(Integer productId);

    Review addReviewToProduct(Integer productId, ReviewRequestDTO reviewRequest);

    // For testing: write review without authentication
    Review addReviewToProductForTest(Integer productId, ReviewRequestDTO reviewRequest);

    // --- Admin-facing features ---
    List<Product> getAllProducts();

    Page<Product> getAllProducts(Pageable pageable);

    Product createProduct(ProductRequestDTO product);

    Optional<Product> updateProduct(Integer id, ProductRequestDTO productDetails);

    void deleteProduct(Integer id);

    Optional<Product> updateStock(Integer id, Integer newStock);

    Optional<Product> setProductVisibility(Integer id, boolean isVisible);

    /**
     * Update product image: store image by product ID and update DB path; return
     * updated product
     */
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