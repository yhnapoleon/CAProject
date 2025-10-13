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

    // 获取所有上架商品 (按需返回全部列表)
    @GetMapping("/getVisibleProducts")
    public ResponseEntity<List<Product>> getVisibleProducts() {
        return ResponseEntity.ok(productService.getAllVisibleProducts());
    }

    // 获取所有上架商品 (分页版)
    @GetMapping
    public ResponseEntity<Page<Product>> getVisibleProductsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.getAllVisibleProducts(pageable));
    }

    // 获取单个商品详情
    @GetMapping("/getProductById/{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // 获取某个商品的所有评论
    @GetMapping("/getReviewsByProductId/{id}")
    public ResponseEntity<List<Review>> getProductReviews(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getReviewsByProductId(id));
    }

    // 为某个商品添加评论 (需要用户认证)
    @PostMapping("/addReviewToProduct/{id}")
    public ResponseEntity<Review> addReview(@PathVariable Integer id, @RequestBody ReviewRequestDTO reviewRequest) {
        // 注意：实际项目中需要从安全上下文中获取当前用户并设置到 review 对象中
        Review savedReview = productService.addReviewToProduct(id, reviewRequest);
        return ResponseEntity.status(201).body(savedReview);
    }

    // 临时测试端点：跳过鉴权，使用硬编码测试用户
    @PostMapping("/test/addReviewToProduct/{id}")
    public ResponseEntity<Review> addReviewForTest(@PathVariable Integer id,
            @RequestBody ReviewRequestDTO reviewRequest) {
        Review savedReview = productService.addReviewToProductForTest(id, reviewRequest);
        return ResponseEntity.status(201).body(savedReview);
    }

    // 将商品加入购物车 (需要前端传递 userId 与数量)
    @PostMapping("/addToCart/{id}")
    public ResponseEntity<ShoppingCart> addToCart(@PathVariable Integer id,
                                                  @RequestParam Integer userId,
                                                  @RequestBody AddToCartRequestDTO request) {
        ShoppingCart item = cartService.addProductToCart(userId, id, request.getQuantity());
        return ResponseEntity.status(201).body(item);
    }

    // 测试端点：将商品加入购物车（固定测试用户ID=100031）
    @PostMapping("/test/addToCart/{id}")
    public ResponseEntity<ShoppingCart> addToCartForTest(@PathVariable Integer id,
                                                         @RequestBody AddToCartRequestDTO request) {
        ShoppingCart item = cartService.addProductToCart(100031, id, request.getQuantity());
        return ResponseEntity.status(201).body(item);
    }
}