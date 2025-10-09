package sg.nusiss.t6.caproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sg.nusiss.t6.caproject.model.UserCoupon;
import sg.nusiss.t6.caproject.service.CouponService;
import sg.nusiss.t6.caproject.util.Code;
import sg.nusiss.t6.caproject.util.DataResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponService CouponService;

    @GetMapping("/getUserCoupons/{userId}")
    public DataResult getUserCoupons(@PathVariable Integer userId) {
        try {
            List<UserCoupon> userCoupons = CouponService.getUserCouponsByUserId(userId);

            // 转换为简化的DTO格式
            List<Map<String, Object>> data = userCoupons.stream()
                    .map(uc -> {
                        Map<String, Object> couponInfo = new HashMap<>();
                        couponInfo.put("userCouponId", uc.getUserCouponId());
                        couponInfo.put("couponId", uc.getCoupon().getCouponId());
                        couponInfo.put("couponNumber", uc.getCoupon().getCouponNumber());
                        couponInfo.put("couponValue", uc.getCoupon().getCouponValue());
                        couponInfo.put("couponStartTime", uc.getCoupon().getCouponStartTime());
                        couponInfo.put("couponEndTime", uc.getCoupon().getCouponEndTime());
                        couponInfo.put("couponName", uc.getCoupon().getCouponName());
                        couponInfo.put("status", uc.getCouponStatus());
                        couponInfo.put("isValid", uc.getCouponStatus() == 1); // 添加是否有效的布尔值
                        return couponInfo;
                    })
                    .collect(Collectors.toList());

            return new DataResult(Code.SUCCESS, data, "获取用户优惠券列表成功");
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "获取用户优惠券列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/addUserCoupon")
    public DataResult addUserCoupon(@RequestBody Map<String, Object> request) {
        try {
            Integer userId = (Integer) request.get("userId");
            Integer couponId = (Integer) request.get("couponId");

            if (userId == null || couponId == null) {
                return new DataResult(Code.FAILED, null, "用户ID和优惠券ID不能为空");
            }

            return CouponService.addUserCoupon(userId, couponId);
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "添加用户优惠券失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteUserCoupon/{userCouponId}")
    public DataResult deleteUserCoupon(@PathVariable Integer userCouponId) {
        try {
            if (userCouponId == null) {
                return new DataResult(Code.FAILED, null, "用户优惠券ID不能为空");
            }

            return CouponService.deleteUserCoupon(userCouponId);
        } catch (Exception e) {
            return new DataResult(Code.FAILED, null, "删除用户优惠券失败: " + e.getMessage());
        }
    }
}
