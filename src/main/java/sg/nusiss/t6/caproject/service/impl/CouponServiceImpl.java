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
			// 1️⃣ 查找用户
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new RuntimeException("用户不存在: " + userId));

			// 2️⃣ 查找优惠券
			Coupon coupon = couponRepository.findById(couponId)
					.orElseThrow(() -> new RuntimeException("优惠券不存在: " + couponId));

			// 3️⃣ 检查是否已经拥有该优惠券
			List<UserCoupon> existingUserCoupons = userCouponRepository.findByUserUserId(userId);
			boolean alreadyOwned = existingUserCoupons.stream()
					.anyMatch(uc -> uc.getCoupon().getCouponId().equals(couponId));

			if (alreadyOwned) {
				return new DataResult(Code.FAILED, null, "用户已经拥有该优惠券");
			}

			// 4️⃣ 创建用户优惠券关联
			UserCoupon userCoupon = new UserCoupon();
			userCoupon.setUser(user);
			userCoupon.setCoupon(coupon);
			userCoupon.setCouponStatus(1); // 默认设置为有效状态

			// 5️⃣ 保存关联
			UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);

			// 6️⃣ 返回结果
			return new DataResult(Code.SUCCESS, savedUserCoupon.getUserCouponId(), "优惠券添加成功");
		} catch (Exception e) {
			return new DataResult(Code.FAILED, null, "优惠券添加失败: " + e.getMessage());
		}
	}

	@Override
	public DataResult deleteUserCoupon(Integer userCouponId) {
		try {
			// 1️⃣ 查找用户优惠券关联
			UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
					.orElseThrow(() -> new RuntimeException("用户优惠券关联不存在: " + userCouponId));

			// 2️⃣ 删除关联
			userCouponRepository.delete(userCoupon);

			// 3️⃣ 返回结果
			return new DataResult(Code.SUCCESS, userCouponId, "优惠券删除成功");
		} catch (Exception e) {
			return new DataResult(Code.FAILED, null, "优惠券删除失败: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public void deleteCoupon(Integer id) {
		couponRepository.deleteById(id);
	}
}
