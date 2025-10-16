//By Ying Hao

package sg.nusiss.t6.caproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nusiss.t6.caproject.model.Product;
import sg.nusiss.t6.caproject.model.Review;
import sg.nusiss.t6.caproject.controller.dto.ReviewRequestDTO;
import sg.nusiss.t6.caproject.controller.dto.ReviewResponseDTO;
import sg.nusiss.t6.caproject.service.ProductService;
import sg.nusiss.t6.caproject.controller.dto.AddToCartRequestDTO;
import sg.nusiss.t6.caproject.model.ShoppingCart;
import sg.nusiss.t6.caproject.service.CartService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public ProductController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    // Get all visible products (return full list as needed)
    @GetMapping("/getVisibleProducts")
    public ResponseEntity<List<Product>> getVisibleProducts() {
        return ResponseEntity.ok(productService.getAllVisibleProducts());
    }

    // Get all visible products (paginated)
    @GetMapping
    public ResponseEntity<Page<Product>> getVisibleProductsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.getAllVisibleProducts(pageable));
    }

    // Get single product details
    @GetMapping("/getProductById/{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // Get all reviews for a product (includes user name and title)
    @GetMapping("/getReviewsByProductId/{id}")
    public ResponseEntity<List<ReviewResponseDTO>> getProductReviews(@PathVariable Integer id) {
        List<ReviewResponseDTO> reviews = productService.getReviewsWithUserNameByProductId(id);
        System.out.println("Returned reviews count: " + reviews.size());
        reviews.forEach(review -> System.out.println("Review: " + review.getReviewId() + ", Title: " + review.getTitle()
                + ", User: " + review.getUserName()));
        return ResponseEntity.ok(reviews);
    }

    // Add a review to a product (requires user authentication)
    @PostMapping("/addReviewToProduct/{id}")
    public ResponseEntity<Review> addReview(@PathVariable Integer id, @RequestBody ReviewRequestDTO reviewRequest) {
        // Note: In real projects, get the current user from the security context and
        // set it on the review
        Review savedReview = productService.addReviewToProduct(id, reviewRequest);
        return ResponseEntity.status(201).body(savedReview);
    }

    // Temporary testing endpoint: skip auth and use a hard-coded test user
    @PostMapping("/test/addReviewToProduct/{id}")
    public ResponseEntity<Review> addReviewForTest(@PathVariable Integer id,
            @RequestBody ReviewRequestDTO reviewRequest) {
        Review savedReview = productService.addReviewToProductForTest(id, reviewRequest);
        return ResponseEntity.status(201).body(savedReview);
    }

    // Add product to cart (frontend passes userId and quantity)
    @PostMapping("/addToCart/{id}")
    public ResponseEntity<ShoppingCart> addToCart(@PathVariable Integer id,
            @RequestParam Integer userId,
            @RequestBody AddToCartRequestDTO request) {
        ShoppingCart item = cartService.addProductToCart(userId, id, request.getQuantity());
        return ResponseEntity.status(201).body(item);
    }

    // Testing endpoint: add product to cart with fixed test user ID = 100031
    @PostMapping("/test/addToCart/{id}")
    public ResponseEntity<ShoppingCart> addToCartForTest(@PathVariable Integer id,
            @RequestBody AddToCartRequestDTO request) {
        ShoppingCart item = cartService.addProductToCart(100031, id, request.getQuantity());
        return ResponseEntity.status(201).body(item);
    }
}