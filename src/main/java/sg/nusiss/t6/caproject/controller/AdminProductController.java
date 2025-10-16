//By Ying Hao

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

// In a real project, this controller should be protected by Spring Security and accessible to admins only
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

    // Get all products, including those not visible (unpublished)
    @GetMapping("/getAllProducts")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // Get all products (paginated)
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProductsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    // Create a new product (JSON request, imageUrl passed as a link)
    @PostMapping("/createProduct")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequestDTO product) {
        // Sanitize imageUrl (remove placeholders); do not convert to absolute HTTP URL
        product.setImageUrl(sanitizeImageUrl(product.getImageUrl()));
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(201).body(createdProduct);
    }

    // Create a new product (multipart form, supports direct image upload)
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
            // Create the product first to get its ID
            Product temp = productService.createProduct(product);
            // Use product ID as the filename (preserve extension)
            String original = actual.getOriginalFilename() != null ? actual.getOriginalFilename() : "";
            String ext = "";
            int idx = original.lastIndexOf('.');
            if (idx >= 0)
                ext = original.substring(idx);
            String filename = temp.getProductId() + ext; // e.g., 200123.jpg
            fileStorageService.storeProductImageWithName(actual, filename);
            // Store relative public URL in DB: /images/{productId}.{ext}
            String publicUrl = buildRelativePublicUrl(filename);
            productService.updateProductImage(temp.getProductId(), publicUrl);
            return ResponseEntity.status(201).body(temp);
        }
        // If no file is uploaded but the body contains imageUrl, also sanitize (remove
        // placeholders)
        product.setImageUrl(sanitizeImageUrl(product.getImageUrl()));

        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(201).body(createdProduct);
    }

    // New: Update product image (name by product ID and overwrite existing image)
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

    // New: Upload image only and return the generated filename and local absolute
    // path
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

    // Update product information (DTO)
    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<Optional<Product>> updateProduct(@PathVariable Integer id,
            @RequestBody ProductRequestDTO productDetails) {
        productDetails.setImageUrl(sanitizeImageUrl(productDetails.getImageUrl()));
        return ResponseEntity.ok(productService.updateProduct(id, productDetails));
    }

    // Delete product
    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Update stock
    @PatchMapping("/updateStock/{id}")
    public ResponseEntity<Optional<Product>> updateStock(@PathVariable Integer id,
            @RequestBody Map<String, Integer> stockUpdate) {
        // Support two field names
        Integer newStock = stockUpdate.get("stock");
        if (newStock == null) {
            newStock = stockUpdate.get("stockQuantity");
        }
        if (newStock == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productService.updateStock(id, newStock));
    }

    // Toggle product visibility
    @PatchMapping("/setVisibility/{id}")
    public ResponseEntity<Optional<Product>> setVisibility(@PathVariable Integer id,
            @RequestBody Map<String, Boolean> visibilityUpdate) {
        Boolean isVisible = visibilityUpdate.get("isVisible");
        if (isVisible == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productService.setProductVisibility(id, isVisible));
    }

    // Download endpoint: read the local path from DB by product ID and return the
    // file
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

    // Filter placeholders or empty strings; do not convert to an absolute HTTP URL
    private String sanitizeImageUrl(String raw) {
        if (raw == null)
            return null;
        String url = raw.trim();
        if (url.isEmpty())
            return null;
        // Ignore common placeholders/mock design paths
        if ("/images/placeholder.svg".equals(url) || url.contains("/123/images/")) {
            return null;
        }
        // Keep /images/ relative path as-is; the frontend will resolve host and port
        if (url.startsWith("/images/")) {
            return url;
        }
        // Windows local path → extract filename → absolute URL
        if (url.matches("^[A-Za-z]:\\\\.*") || url.startsWith("\\\\")) {
            String filename = new java.io.File(url).getName();
            return buildRelativePublicUrl(filename);
        }
        // Return others as-is (may already be a full URL)
        return url;
    }

    private String buildRelativePublicUrl(String filename) {
        return "/images/" + filename;
    }
}