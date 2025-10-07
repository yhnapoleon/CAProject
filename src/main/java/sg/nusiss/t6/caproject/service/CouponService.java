package sg.nusiss.t6.caproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sg.nusiss.t6.caproject.model.Coupon;

import java.util.List;
import java.util.Optional;

public interface CouponService {

	List<Coupon> getAllCoupons();

	Page<Coupon> getAllCoupons(Pageable pageable);

	Optional<Coupon> getCouponById(Integer id);

	Coupon createCoupon(Coupon coupon);

	Optional<Coupon> updateCoupon(Integer id, Coupon couponDetails);

	void deleteCoupon(Integer id);
}


