package sg.nusiss.t6.caproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sg.nusiss.t6.caproject.model.ShoppingCart;

import java.util.List;

public interface CartRepository extends JpaRepository<ShoppingCart,Integer> {

    @Query("select sc from ShoppingCart sc join sc.product where sc.user.userId = :userId")
    List<ShoppingCart> findCartItemByUserId(Integer userId);


}
