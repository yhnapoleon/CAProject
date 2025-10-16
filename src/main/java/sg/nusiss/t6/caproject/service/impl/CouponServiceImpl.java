package sg.nusiss.t6.caproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nusiss.t6.caproject.model.Coupon;
import sg.nusiss.t6.caproject.model.User;
import sg.nusiss.t6.caproject.model.UserCoupon;
import sg.nusiss.t6.caproject.repository.CouponRepository;
import sg.nusiss.t6.caproject.repository.UserCouponRepository;
import sg.nusiss.t6.caproject.repository.UserRepository;
import sg.nusiss.t6.caproject.service.CouponService;
import sg.nusiss.t6.caproject.util.Code;
import sg.nusiss.t6.caproject.util.DataResult;
import sg.nusiss.t6.caproject.controller.dto.CouponRequestDTO;

import java.util.List;
import java.util.Optional;

@Service
public class CouponServiceImpl implements CouponService {

	@Autowired
	private final CouponRepository couponRepository;

	@Autowired
	private UserCouponRepository userCouponRepository;

	@Autowired
	private UserRepository userRepository;

	public CouponServiceImpl(CouponRepository couponRepository) {
		this.couponRepository = couponRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Coupon> getAllCoupons() {
		return couponRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Coupon> getAllCoupons(Pageable pageable) {
		return couponRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Coupon> getCouponById(Integer id) {
		return couponRepository.findById(id);
	}

	@Override
	@Transactional
	public Coupon createCoupon(CouponRequestDTO coupon) {
		Coupon entity = new Coupon();
		entity.setCouponNumber(coupon.getCouponNumber());
		entity.setCouponValue(coupon.getCouponValue());
		entity.setCouponStartTime(coupon.getCouponStartTime());
		entity.setCouponEndTime(coupon.getCouponEndTime());
		entity.setCouponName(coupon.getCouponName());
		return couponRepository.save(entity);
	}

	@Override
	@Transactional
	public Optional<Coupon> updateCoupon(Integer id, CouponRequestDTO couponDetails) {
		return couponRepository.findById(id).map(existing -> {
			existing.setCouponName(couponDetails.getCouponName());
			existing.setCouponNumber(couponDetails.getCouponNumber());
			existing.setCouponValue(couponDetails.getCouponValue());
			existing.setCouponStartTime(couponDetails.getCouponStartTime());
			existing.setCouponEndTime(couponDetails.getCouponEndTime());
			return couponRepository.save(existing);
		});
	}

	@Override
	public List<UserCoupon> getUserCouponsByUserId(Integer userId) {
		return userCouponRepository.findByUserUserId(userId);
	}

	@Override
	public DataResult addUserCoupon(Integer userId, Integer couponId) {
		try {
			// 1) Find user
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new RuntimeException("User not found: " + userId));

			// 2) Find coupon
			Coupon coupon = couponRepository.findById(couponId)
					.orElseThrow(() -> new RuntimeException("Coupon not found: " + couponId));

			// 3) Check whether the user already owns this coupon
			List<UserCoupon> existingUserCoupons = userCouponRepository.findByUserUserId(userId);
			boolean alreadyOwned = existingUserCoupons.stream()
					.anyMatch(uc -> uc.getCoupon().getCouponId().equals(couponId));

			if (alreadyOwned) {
				return new DataResult(Code.FAILED, null, "User already owns this coupon");
			}

			// 4) Create user-coupon association
			UserCoupon userCoupon = new UserCoupon();
			userCoupon.setUser(user);
			userCoupon.setCoupon(coupon);
			userCoupon.setCouponStatus(1); // Set as valid by default

			// 5) Save association
			UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);

			// 6) Return result
			return new DataResult(Code.SUCCESS, savedUserCoupon.getUserCouponId(), "Coupon added successfully");
		} catch (Exception e) {
			return new DataResult(Code.FAILED, null, "Failed to add coupon: " + e.getMessage());
		}
	}

	@Override
	public DataResult deleteUserCoupon(Integer userCouponId) {
		try {
			// 1) Find user-coupon association
			UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
					.orElseThrow(() -> new RuntimeException("User-coupon association not found: " + userCouponId));

			// 2) Delete association
			userCouponRepository.delete(userCoupon);

			// 3) Return result
			return new DataResult(Code.SUCCESS, userCouponId, "Coupon deleted successfully");
		} catch (Exception e) {
			return new DataResult(Code.FAILED, null, "Failed to delete coupon: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public void deleteCoupon(Integer id) {
		couponRepository.deleteById(id);
	}
}
