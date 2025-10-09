package sg.nusiss.t6.caproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nusiss.t6.caproject.model.ShoppingCart;
import sg.nusiss.t6.caproject.repository.CartRepository;
import sg.nusiss.t6.caproject.service.CartService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<ShoppingCart> getCartItemByUserId(Integer userId){
        return cartRepository.findCartItemByUserId(userId);
    }

    @Override
    public void updateCartItemQuantity(Integer shoppingCartId, Integer quantity) {
        Optional<ShoppingCart> cartOptional = cartRepository.findById(shoppingCartId);
        if(cartOptional.isPresent()){
            ShoppingCart item = cartOptional.get();
            item.setQuantity(quantity);
            cartRepository.save(item);
        }else {
            throw new RuntimeException("cartItem does not exist which id = "+shoppingCartId);
        }
    }

    @Override
    public int deleteCartItems(List<Integer> shoppingCartIds) {
        int count = 0;
        for (Integer id : shoppingCartIds) {
            if (cartRepository.existsById(id)) {
                cartRepository.deleteById(id);
                count++;
            }
        }
        if (count == 0) {
            throw new RuntimeException("没有找到对应的购物车项进行删除");
        }
        return count;
    }
}
