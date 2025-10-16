package sg.nusiss.t6.caproject.controller.dto;

import lombok.Data;

@Data
public class AddToCartRequestDTO {

    // 加入购物车的数量
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}


