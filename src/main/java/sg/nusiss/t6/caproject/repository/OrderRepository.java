//By Zhao Jiayi

package sg.nusiss.t6.caproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sg.nusiss.t6.caproject.model.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("select o from Order o left join o.orderItems oi left join oi.product where o.user.userId = :userId")
    List<Order> findOrdersByUserId(Integer userId);
}
