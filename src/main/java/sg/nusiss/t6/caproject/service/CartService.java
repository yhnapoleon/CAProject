package sg.nusiss.t6.caproject.service;

import sg.nusiss.t6.caproject.model.ShoppingCart;

import java.util.List;

public interface CartService {
    List<ShoppingCart> getCartItemByUserId(Integer userId);

    void updateCartItemQuantity(Integer shoppingCartId, Integer quantity);

    int deleteCartItems(List<Integer> shoppingCartIds);

    // 加入购物车：若已存在则累加数量，否则新建
    ShoppingCart addProductToCart(Integer userId, Integer productId, Integer quantity);

}
