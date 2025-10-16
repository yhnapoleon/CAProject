//By Zhao Jiayi

package sg.nusiss.t6.caproject.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class OrderRequestDTO {
    // Getters and Setters for OrderRequestDTO
    private Integer userId;
    private LocalDateTime orderTime;
    private Integer orderStatus;
    private Integer discountId;
    private Integer couponId;
    private Integer deliveryType;
    private String deliveryLocation;
    private List<OrderItemDTO> items;

    @Setter
    @Getter
    public static class OrderItemDTO {
        // Getters and Setters for OrderItemDTO
        private Integer productId;
        private Integer quantity;
        private Integer unitPrice;

    }

}

