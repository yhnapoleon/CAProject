package sg.nusiss.t6.caproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
// import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
        // 清洗 imageUrl（去除占位符）；不再转换为 HTTP 绝对地址
        product.setImageUrl(sanitizeImageUrl(product.getImageUrl()));
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(201).body(createdProduct);
    }

    // 添加新商品（multipart 表单，支持直接上传图片文件）
    @PostMapping(value = "/createProductWithImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> createProductWithImage(
            @RequestPart("product") ProductRequestDTO product,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "picture", required = false) MultipartFile picture) {

        MultipartFile actual = image != null && !image.isEmpty() ? image
                : file != null && !file.isEmpty() ? file
                        : picture;

        if (actual != null && !actual.isEmpty()) {
            // 先创建商品以获取ID
            Product temp = productService.createProduct(product);
            // 用商品ID作为文件名（保持扩展名）
            String original = actual.getOriginalFilename() != null ? actual.getOriginalFilename() : "";
            String ext = "";
            int idx = original.lastIndexOf('.');
            if (idx >= 0)
                ext = original.substring(idx);
            String filename = temp.getProductId() + ext; // 例如 200123.jpg
            fileStorageService.storeProductImageWithName(actual, filename);
            // 数据库存相对公共URL：/images/{productId}.{ext}
            String publicUrl = buildRelativePublicUrl(filename);
            productService.updateProductImage(temp.getProductId(), publicUrl);
            return ResponseEntity.status(201).body(temp);
        }
        // 如果未上传文件，但 body 带了 imageUrl，同样做清洗（去占位符）
        product.setImageUrl(sanitizeImageUrl(product.getImageUrl()));

        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(201).body(createdProduct);
    }

    // 新增：更新商品图片（使用商品ID命名并覆盖原图）
    @PostMapping(value = "/updateImage/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@PathVariable Integer id,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "picture", required = false) MultipartFile picture) {

        MultipartFile actualFile = image != null && !image.isEmpty() ? image
                : file != null && !file.isEmpty() ? file
                        : picture;

        if (actualFile == null || actualFile.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Image file is required."));
        }

        Optional<Product> updatedProduct = productService.updateProductImage(id, actualFile);
        return updatedProduct.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 新增：仅上传图片，返回自动生成的文件名与本地绝对路径
    @PostMapping(value = "/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadImageOnly(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "picture", required = false) MultipartFile picture) {

        MultipartFile actual = image != null && !image.isEmpty() ? image
                : file != null && !file.isEmpty() ? file
                        : picture;
        if (actual == null || actual.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String storedPath = fileStorageService.storeProductImage(actual);
        String filename = new java.io.File(storedPath).getName();
        return ResponseEntity.ok(java.util.Map.of(
                "filename", filename,
                "path", storedPath));
    }

    // 更新商品信息（使用DTO）
    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<Optional<Product>> updateProduct(@PathVariable Integer id,
            @RequestBody ProductRequestDTO productDetails) {
        productDetails.setImageUrl(sanitizeImageUrl(productDetails.getImageUrl()));
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

    // 下载接口：根据产品ID读取数据库中的本地路径并回传文件
    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> getProductImage(@PathVariable Integer id) {
        Optional<Product> opt = productService.getProductById(id);
        if (opt.isEmpty() || opt.get().getImageUrl() == null || opt.get().getImageUrl().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String localPath = opt.get().getImageUrl();
        FileSystemResource resource = new FileSystemResource(localPath);
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        String filename = resource.getFilename();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    // 过滤占位符或空串；不做 HTTP 绝对地址转换
    private String sanitizeImageUrl(String raw) {
        if (raw == null)
            return null;
        String url = raw.trim();
        if (url.isEmpty())
            return null;
        // 忽略常见占位符/设计稿路径
        if ("/images/placeholder.svg".equals(url) || url.contains("/123/images/")) {
            return null;
        }
        // /images/ 相对路径保持原样，由前端按当前主机与端口访问
        if (url.startsWith("/images/")) {
            return url;
        }
        // Windows 本地路径 → 提取文件名 → 绝对 URL
        if (url.matches("^[A-Za-z]:\\\\.*") || url.startsWith("\\\\")) {
            String filename = new java.io.File(url).getName();
            return buildRelativePublicUrl(filename);
        }
        // 其余按原样返回（可为完整 URL）
        return url;
    }

    private String buildRelativePublicUrl(String filename) {
        return "/images/" + filename;
    }
}