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
    public DataResult getCartItem(@PathVariable Integer userId){
        try {
            List<ShoppingCart> cartItems = cartService.getCartItemByUserId(userId);

            // 转换为DTO格式
            List<CartItemResponseDTO> responseDTOs = cartItems.stream()
                    .map(cart -> {
                        CartItemResponseDTO dto = new CartItemResponseDTO();
                        dto.setShoppingCartId(cart.getShoppingCartId());
                        dto.setQuantity(cart.getQuantity());

                        // 设置商品信息
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

            return new DataResult(Code.SUCCESS, responseDTOs, "获取购物车商品成功");
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "获取购物车商品失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateCartItemQuantity")
    public DataResult updateCartQuantity(@RequestBody Map<String,Integer> request){
        try {
            Integer shoppingCartId = request.get("shoppingCartId");
            Integer quantity = request.get("quantity");
            cartService.updateCartItemQuantity(shoppingCartId,quantity);
            return new DataResult(Code.SUCCESS, null, "更新商品数量成功");
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "更新商品数量失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteCartItem")
    public DataResult deleteCartItems(@RequestBody List<Integer> shoppingCartIds) {
        try {
            int deletedCount = cartService.deleteCartItems(shoppingCartIds);
            return new DataResult(Code.SUCCESS, deletedCount, "成功删除 " + deletedCount + " 个商品");
        } catch (RuntimeException e) {
            return new DataResult(Code.FAILED, null, "删除商品失败: " + e.getMessage());
        }
    }


}
