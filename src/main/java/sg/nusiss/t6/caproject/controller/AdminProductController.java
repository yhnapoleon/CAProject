package sg.nusiss.t6.caproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import sg.nusiss.t6.caproject.model.Product;
import sg.nusiss.t6.caproject.service.ProductService;
import sg.nusiss.t6.caproject.controller.dto.ProductRequestDTO;
import sg.nusiss.t6.caproject.service.FileStorageService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

// 实际项目中，这个 Controller 应受 Spring Security 保护，只允许管理员访问
@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final FileStorageService fileStorageService;

    @Autowired
    public AdminProductController(ProductService productService, FileStorageService fileStorageService) {
        this.productService = productService;
        this.fileStorageService = fileStorageService;
    }

    // 获取所有商品，包括未上架的
    @GetMapping("/getAllProducts")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 获取所有商品（分页）
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProductsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    // 添加新商品（JSON 请求，imageUrl 直接传链接）
    @PostMapping("/createProduct")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequestDTO product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(201).body(createdProduct);
    }

    // 添加新商品（multipart 表单，支持直接上传图片文件）
    @PostMapping(value = "/createProductWithImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> createProductWithImage(
            @RequestPart("product") ProductRequestDTO product,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        if (image != null && !image.isEmpty()) {
            String imageUrl = fileStorageService.storeProductImage(image);
            product.setImageUrl(imageUrl);
        }

        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(201).body(createdProduct);
    }

    // 更新商品信息（使用DTO）
    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<Optional<Product>> updateProduct(@PathVariable Integer id,
            @RequestBody ProductRequestDTO productDetails) {
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
    public ResponseEntity<Optional<Product>> updateStock(@PathVariable Integer id,
            @RequestBody Map<String, Integer> stockUpdate) {
        // 兼容两种字段名
        Integer newStock = stockUpdate.get("stock");
        if (newStock == null) {
            newStock = stockUpdate.get("stockQuantity");
        }
        if (newStock == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productService.updateStock(id, newStock));
    }

    // 上架/下架商品
    @PatchMapping("/setVisibility/{id}")
    public ResponseEntity<Optional<Product>> setVisibility(@PathVariable Integer id,
            @RequestBody Map<String, Boolean> visibilityUpdate) {
        Boolean isVisible = visibilityUpdate.get("isVisible");
        if (isVisible == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productService.setProductVisibility(id, isVisible));
    }
}