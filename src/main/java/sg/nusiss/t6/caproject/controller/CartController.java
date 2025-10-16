package sg.nusiss.t6.caproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nusiss.t6.caproject.controller.dto.CartItemResponseDTO;
import sg.nusiss.t6.caproject.model.ShoppingCart;
import sg.nusiss.t6.caproject.service.CartService;
import sg.nusiss.t6.caproject.util.DataResult;
import sg.nusiss.t6.caproject.util.Code;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/getCartItem/{userId}")
    public DataResult getCartItem(@PathVariable Integer userId) {
        try {
            List<ShoppingCart> cartItems = cartService.getCartItemByUserId(userId);

            // Convert to DTO format
            List<CartItemResponseDTO> responseDTOs = cartItems.stream()
                    .map(cart -> {
                        CartItemResponseDTO dto = new CartItemResponseDTO();
                        dto.setShoppingCartId(cart.getShoppingCartId());
                        dto.setQuantity(cart.getQuantity());

                        // Set product info
                        CartItemResponseDTO.ProductInfo productInfo = new CartItemResponseDTO.ProductInfo();
                        productInfo.setProductId(cart.getProduct().getProductId());
                        productInfo.setProductName(cart.getProduct().getProductName());
                        productInfo.setProductDescription(cart.getProduct().getProductDescription());
                        productInfo.setProductPrice(cart.getProduct().getProductPrice().doubleValue());
                        productInfo.setProductStockQuantity(cart.getProduct().getProductStockQuantity());
                        productInfo.setProductCategory(cart.getProduct().getProductCategory());
                        productInfo.setIsVisible(cart.getProduct().getIsVisible());
                        productInfo.setImageUrl(cart.getProduct().getImageUrl());

                        dto.setProduct(productInfo);
                        return dto;
                    })
                    .toList();

            return new DataResult(Code.SUCCESS, responseDTOs, "Fetched cart items successfully");
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "Failed to fetch cart items: " + e.getMessage());
        }
    }

    @PostMapping("/updateCartItemQuantity")
    public DataResult updateCartQuantity(@RequestBody Map<String, Integer> request) {
        try {
            Integer shoppingCartId = request.get("shoppingCartId");
            Integer quantity = request.get("quantity");
            cartService.updateCartItemQuantity(shoppingCartId, quantity);
            return new DataResult(Code.SUCCESS, null, "Updated item quantity successfully");
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "Failed to update item quantity: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteCartItem")
    public DataResult deleteCartItems(@RequestBody List<Integer> shoppingCartIds) {
        try {
            int deletedCount = cartService.deleteCartItems(shoppingCartIds);
            return new DataResult(Code.SUCCESS, deletedCount, "Successfully deleted " + deletedCount + " items");
        } catch (RuntimeException e) {
            return new DataResult(Code.FAILED, null, "Failed to delete items: " + e.getMessage());
        }
    }

}
