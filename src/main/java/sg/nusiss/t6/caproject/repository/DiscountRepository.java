//By Zhao Jiayi

package sg.nusiss.t6.caproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.nusiss.t6.caproject.model.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
}
