package sg.nusiss.t6.caproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nusiss.t6.caproject.model.ShoppingCart;
import sg.nusiss.t6.caproject.model.Product;
import sg.nusiss.t6.caproject.model.User;
import sg.nusiss.t6.caproject.repository.CartRepository;
import sg.nusiss.t6.caproject.repository.ProductRepository;
import sg.nusiss.t6.caproject.repository.UserRepository;
import sg.nusiss.t6.caproject.service.CartService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<ShoppingCart> getCartItemByUserId(Integer userId) {
        return cartRepository.findCartItemByUserId(userId);
    }

    @Override
    public void updateCartItemQuantity(Integer shoppingCartId, Integer quantity) {
        Optional<ShoppingCart> cartOptional = cartRepository.findById(shoppingCartId);
        if (cartOptional.isPresent()) {
            ShoppingCart item = cartOptional.get();
            item.setQuantity(quantity);
            cartRepository.save(item);
        } else {
            throw new RuntimeException("cartItem does not exist which id = " + shoppingCartId);
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
            throw new RuntimeException("No matching cart items found to delete");
        }
        return count;
    }

    @Override
    public ShoppingCart addProductToCart(Integer userId, Integer productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("quantity must be a positive integer");
        }

        // Validate that user and product exist
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        // Check whether there is an existing cart item for the same product
        ShoppingCart existing = cartRepository.findByUserIdAndProductId(userId, productId);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            return cartRepository.save(existing);
        }

        ShoppingCart item = new ShoppingCart();
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(quantity);
        return cartRepository.save(item);
    }
}
