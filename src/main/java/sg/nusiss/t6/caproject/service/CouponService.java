//By Ying Hao
//This feature has not been implemented yet.
package sg.nusiss.t6.caproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sg.nusiss.t6.caproject.model.Coupon;
import sg.nusiss.t6.caproject.model.UserCoupon;
import sg.nusiss.t6.caproject.util.DataResult;
import sg.nusiss.t6.caproject.controller.dto.CouponRequestDTO;

import java.util.List;
import java.util.Optional;

public interface CouponService {

	List<Coupon> getAllCoupons();

	Page<Coupon> getAllCoupons(Pageable pageable);

	Optional<Coupon> getCouponById(Integer id);

	Coupon createCoupon(CouponRequestDTO coupon);

	Optional<Coupon> updateCoupon(Integer id, CouponRequestDTO couponDetails);

	List<UserCoupon> getUserCouponsByUserId(Integer userId);

	DataResult addUserCoupon(Integer userId, Integer couponId);

	DataResult deleteUserCoupon(Integer userCouponId);

	void deleteCoupon(Integer id);
}
