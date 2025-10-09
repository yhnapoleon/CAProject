package sg.nusiss.t6.caproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.nusiss.t6.caproject.model.UserCoupon;

import java.util.List;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Integer> {

    // 根据用户ID查找所有用户优惠券
    List<UserCoupon> findByUserUserId(Integer userId);
}
