package sg.nusiss.t6.caproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nusiss.t6.caproject.model.Product;
import sg.nusiss.t6.caproject.service.ProductService;

import java.util.Map;

// 实际项目中，这个 Controller 应受 Spring Security 保护，只允许管理员访问
@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductService productService;

    @Autowired
    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    // 获取所有商品，包括未上架的 (分页)
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    // 添加新商品
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(201).body(createdProduct);
    }

    // 更新商品信息
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productService.updateProduct(id, productDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 删除商品
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // 修改库存
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable Long id, @RequestBody Map<String, Integer> stockUpdate) {
        Integer newStock = stockUpdate.get("stock");
        if (newStock == null) {
            return ResponseEntity.badRequest().build();
        }
        return productService.updateStock(id, newStock)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 上架/下架商品
    @PatchMapping("/{id}/visibility")
    public ResponseEntity<Product> setVisibility(@PathVariable Long id, @RequestBody Map<String, Boolean> visibilityUpdate) {
        Boolean isVisible = visibilityUpdate.get("isVisible");
        if (isVisible == null) {
            return ResponseEntity.badRequest().build();
        }
        return productService.setProductVisibility(id, isVisible)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}