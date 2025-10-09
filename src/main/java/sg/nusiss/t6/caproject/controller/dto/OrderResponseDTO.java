package sg.nusiss.t6.caproject.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponseDTO {
    private Integer orderId;
    private LocalDateTime orderTime;
    private Integer orderStatus;
    private DiscountInfo discount;
    private CouponInfo coupon;
    private List<OrderItemInfo> orderItems;

    public static class DiscountInfo {
        private Integer discountId;
        private String discountName;
        private String discountDescription;
        private LocalDateTime discountStartTime;
        private LocalDateTime discountEndTime;
        private Double discountDiscount;

        // Getters and Setters
        public Integer getDiscountId() {
            return discountId;
        }

        public void setDiscountId(Integer discountId) {
            this.discountId = discountId;
        }

        public String getDiscountName() {
            return discountName;
        }

        public void setDiscountName(String discountName) {
            this.discountName = discountName;
        }

        public String getDiscountDescription() {
            return discountDescription;
        }

        public void setDiscountDescription(String discountDescription) {
            this.discountDescription = discountDescription;
        }

        public LocalDateTime getDiscountStartTime() {
            return discountStartTime;
        }

        public void setDiscountStartTime(LocalDateTime discountStartTime) {
            this.discountStartTime = discountStartTime;
        }

        public LocalDateTime getDiscountEndTime() {
            return discountEndTime;
        }

        public void setDiscountEndTime(LocalDateTime discountEndTime) {
            this.discountEndTime = discountEndTime;
        }

        public Double getDiscountDiscount() {
            return discountDiscount;
        }

        public void setDiscountDiscount(Double discountDiscount) {
            this.discountDiscount = discountDiscount;
        }
    }

    public static class CouponInfo {
        private Integer couponId;
        private String couponNumber;
        private Integer couponValue;
        private LocalDateTime couponStartTime;
        private LocalDateTime couponEndTime;
        private String couponName;
        private UserInfo user;

        public static class UserInfo {
            private Integer userId;
            private String userName;

            public Integer getUserId() {
                return userId;
            }

            public void setUserId(Integer userId) {
                this.userId = userId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }

        // Getters and Setters
        public Integer getCouponId() {
            return couponId;
        }

        public void setCouponId(Integer couponId) {
            this.couponId = couponId;
        }

        public String getCouponNumber() {
            return couponNumber;
        }

        public void setCouponNumber(String couponNumber) {
            this.couponNumber = couponNumber;
        }

        public Integer getCouponValue() {
            return couponValue;
        }

        public void setCouponValue(Integer couponValue) {
            this.couponValue = couponValue;
        }

        public LocalDateTime getCouponStartTime() {
            return couponStartTime;
        }

        public void setCouponStartTime(LocalDateTime couponStartTime) {
            this.couponStartTime = couponStartTime;
        }

        public LocalDateTime getCouponEndTime() {
            return couponEndTime;
        }

        public void setCouponEndTime(LocalDateTime couponEndTime) {
            this.couponEndTime = couponEndTime;
        }

        public String getCouponName() {
            return couponName;
        }

        public void setCouponName(String couponName) {
            this.couponName = couponName;
        }

        public UserInfo getUser() {
            return user;
        }

        public void setUser(UserInfo user) {
            this.user = user;
        }
    }

    public static class OrderItemInfo {
        private Integer orderItemId;
        private ProductInfo product;
        private Integer quantity;
        private Integer unitPrice;

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
        public Integer getOrderItemId() {
            return orderItemId;
        }

        public void setOrderItemId(Integer orderItemId) {
            this.orderItemId = orderItemId;
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

        public Integer getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(Integer unitPrice) {
            this.unitPrice = unitPrice;
        }
    }

    // Getters and Setters
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public DiscountInfo getDiscount() {
        return discount;
    }

    public void setDiscount(DiscountInfo discount) {
        this.discount = discount;
    }

    public CouponInfo getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponInfo coupon) {
        this.coupon = coupon;
    }

    public List<OrderItemInfo> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemInfo> orderItems) {
        this.orderItems = orderItems;
    }
}
