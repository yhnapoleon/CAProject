//By Zhao Jiayi

package sg.nusiss.t6.caproject.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemResponseDTO {
    // Getters and Setters
    private Integer shoppingCartId;
    private ProductInfo product;
    private Integer quantity;

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
