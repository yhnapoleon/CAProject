package sg.nusiss.t6.caproject.service;

import sg.nusiss.t6.caproject.model.ShoppingCart;

import java.util.List;

public interface CartService {
    List<ShoppingCart> getCartItemByUserId(Integer userId);

    void updateCartItemQuantity(Integer shoppingCartId, Integer quantity);

    int deleteCartItems(List<Integer> shoppingCartIds);

    // Add to cart: increase quantity if exists; otherwise create new item
    ShoppingCart addProductToCart(Integer userId, Integer productId, Integer quantity);

}
