//By Zhao Jiayi
//This feature has not been implemented yet.

package sg.nusiss.t6.caproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.nusiss.t6.caproject.model.UserCoupon;

import java.util.List;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Integer> {

    // Find all user coupons by user ID
    List<UserCoupon> findByUserUserId(Integer userId);
}
