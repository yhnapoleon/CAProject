package sg.nusiss.t6.caproject.controller.dto;

import lombok.Data;

@Data
public class AddToCartRequestDTO {

    // Quantity to add to cart
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
