package sg.nusiss.t6.caproject.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nusiss.t6.caproject.model.Coupon;
import sg.nusiss.t6.caproject.repository.CouponRepository;
import sg.nusiss.t6.caproject.service.CouponService;

import java.util.List;
import java.util.Optional;

@Service
public class CouponServiceImpl implements CouponService {

	private final CouponRepository couponRepository;

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
	public Coupon createCoupon(Coupon coupon) {
		return couponRepository.save(coupon);
	}

	@Override
	@Transactional
	public Optional<Coupon> updateCoupon(Integer id, Coupon couponDetails) {
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
	@Transactional
	public void deleteCoupon(Integer id) {
		couponRepository.deleteById(id);
	}
}


