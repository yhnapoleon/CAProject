package sg.nusiss.t6.caproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nusiss.t6.caproject.model.Product;
import sg.nusiss.t6.caproject.service.ProductService;

import java.util.List;
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

    // 获取所有商品，包括未上架的
    @GetMapping("/getAllProducts")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 添加新商品
    @PostMapping("/createProduct")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(201).body(createdProduct);
    }

    // 更新商品信息
    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product productDetails) {
        return ResponseEntity.ok(productService.updateProduct(id, productDetails));
    }

    // 删除商品
    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // 修改库存
    @PatchMapping("/updateStock/{id}")
    public ResponseEntity<Product> updateStock(@PathVariable Integer id,
            @RequestBody Map<String, Integer> stockUpdate) {
        Integer newStock = stockUpdate.get("stock");
        if (newStock == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productService.updateStock(id, newStock));
    }

    // 上架/下架商品
    @PatchMapping("/setVisibility/{id}")
    public ResponseEntity<Product> setVisibility(@PathVariable Integer id,
            @RequestBody Map<String, Boolean> visibilityUpdate) {
        Boolean isVisible = visibilityUpdate.get("isVisible");
        if (isVisible == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productService.setProductVisibility(id, isVisible));
    }
}