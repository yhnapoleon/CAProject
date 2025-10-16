//By Zhao Jiayi

package sg.nusiss.t6.caproject.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class OrderResponseDTO {
    // Getters and Setters
    private Integer orderId;
    private LocalDateTime orderTime;
    private Integer orderStatus;
    private DiscountInfo discount;
    private CouponInfo coupon;
    private List<OrderItemInfo> orderItems;

    @Setter
    @Getter
    public static class DiscountInfo {
        // Getters and Setters
        private Integer discountId;
        private String discountName;
        private String discountDescription;
        private LocalDateTime discountStartTime;
        private LocalDateTime discountEndTime;
        private Double discountDiscount;

    }

    @Setter
    @Getter
    public static class CouponInfo {
        // Getters and Setters
        private Integer couponId;
        private String couponNumber;
        private Integer couponValue;
        private LocalDateTime couponStartTime;
        private LocalDateTime couponEndTime;
        private String couponName;
        private UserInfo user;

        @Setter
        @Getter
        public static class UserInfo {
            private Integer userId;
            private String userName;

        }

    }

    @Setter
    @Getter
    public static class OrderItemInfo {
        // Getters and Setters
        private Integer orderItemId;
        private ProductInfo product;
        private Integer quantity;
        private Integer unitPrice;

        @Setter
        @Getter
        public static class ProductInfo {
            // Getters and Setters
            private Integer productId;
            private String productName;
            private String productDescription;
            private Double productPrice;
            private Integer productStockQuantity;
            private String productCategory;
            private Integer isVisible;
            private String imageUrl;

        }

    }

}
