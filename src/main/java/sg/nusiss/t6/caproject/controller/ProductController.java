package sg.nusiss.t6.caproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nusiss.t6.caproject.model.Product;
import sg.nusiss.t6.caproject.model.Review;
import sg.nusiss.t6.caproject.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 获取所有上架商品 (按需返回全部列表)
    @GetMapping("/getVisibleProducts")
    public ResponseEntity<List<Product>> getVisibleProducts() {
        return ResponseEntity.ok(productService.getAllVisibleProducts());
    }

    // 获取单个商品详情
    @GetMapping("/getProductById/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // 获取某个商品的所有评论
    @GetMapping("/getReviewsByProductId/{id}")
    public ResponseEntity<List<Review>> getProductReviews(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getReviewsByProductId(id));
    }

    // 为某个商品添加评论 (需要用户认证)
    @PostMapping("/addReviewToProduct/{id}")
    public ResponseEntity<Review> addReview(@PathVariable Integer id, @RequestBody Review review) {
        // 注意：实际项目中需要从安全上下文中获取当前用户并设置到 review 对象中
        Review savedReview = productService.addReviewToProduct(id, review);
        return ResponseEntity.status(201).body(savedReview);
    }
}