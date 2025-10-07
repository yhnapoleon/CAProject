package sg.nusiss.t6.caproject.controller.dto;

public class CartItemResponseDTO {
    private Integer shoppingCartId;
    private ProductInfo product;
    private Integer quantity;

    public static class ProductInfo {
        private Integer productId;
        private String productName;
        private String productDescription;
        private Double productPrice;
        private Integer productStockQuantity;
        private String productCategory;
        private Integer isVisible;
        private String imageUrl;

        // Getters and Setters
        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductDescription() {
            return productDescription;
        }

        public void setProductDescription(String productDescription) {
            this.productDescription = productDescription;
        }

        public Double getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(Double productPrice) {
            this.productPrice = productPrice;
        }

        public Integer getProductStockQuantity() {
            return productStockQuantity;
        }

        public void setProductStockQuantity(Integer productStockQuantity) {
            this.productStockQuantity = productStockQuantity;
        }

        public String getProductCategory() {
            return productCategory;
        }

        public void setProductCategory(String productCategory) {
            this.productCategory = productCategory;
        }

        public Integer getIsVisible() {
            return isVisible;
        }

        public void setIsVisible(Integer isVisible) {
            this.isVisible = isVisible;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    // Getters and Setters
    public Integer getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(Integer shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public ProductInfo getProduct() {
        return product;
    }

    public void setProduct(ProductInfo product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
