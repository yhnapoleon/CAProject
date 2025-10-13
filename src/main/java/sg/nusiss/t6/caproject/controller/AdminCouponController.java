package sg.nusiss.t6.caproject.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sg.nusiss.t6.caproject.model.Coupon;
import sg.nusiss.t6.caproject.service.CouponService;
import sg.nusiss.t6.caproject.controller.dto.CouponRequestDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/coupons")
public class AdminCouponController {

	private final CouponService couponService;

	public AdminCouponController(CouponService couponService) {
		this.couponService = couponService;
	}

	@GetMapping("/getAllCoupons")
	public ResponseEntity<List<Coupon>> getAllCoupons() {
		return ResponseEntity.ok(couponService.getAllCoupons());
	}

	@GetMapping
	public ResponseEntity<Page<Coupon>> getAllCouponsPaged(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(couponService.getAllCoupons(pageable));
	}

	@GetMapping("/getCoupon/{id}")
	public ResponseEntity<?> getCoupon(@PathVariable Integer id) {
		Optional<Coupon> coupon = couponService.getCouponById(id);
		return coupon.<ResponseEntity<?>>map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping("/createCoupon")
	public ResponseEntity<Coupon> createCoupon(@RequestBody CouponRequestDTO coupon) {
		Coupon created = couponService.createCoupon(coupon);
		return ResponseEntity.status(201).body(created);
	}

	@PutMapping("/updateCoupon/{id}")
	public ResponseEntity<?> updateCoupon(@PathVariable Integer id, @RequestBody CouponRequestDTO coupon) {
		Optional<Coupon> updated = couponService.updateCoupon(id, coupon);
		return updated.<ResponseEntity<?>>map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/deleteCoupon/{id}")
	public ResponseEntity<Void> deleteCoupon(@PathVariable Integer id) {
		couponService.deleteCoupon(id);
		return ResponseEntity.noContent().build();
	}
}
