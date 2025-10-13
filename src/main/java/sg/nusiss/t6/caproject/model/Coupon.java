package sg.nusiss.t6.caproject.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@Entity
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Integer couponId;

    @Column(name = "coupon_number", length = 20)
    private String couponNumber;

    @Column(name = "coupon_value", nullable = false)
    private Integer couponValue;

    @Column(name = "coupon_start_time", nullable = false)
    private LocalDateTime couponStartTime;

    @Column(name = "coupon_end_time", nullable = false)
    private LocalDateTime couponEndTime;

    @Column(name = "coupon_name", length = 50)
    private String couponName;

    // 一个优惠券可以被多个订单使用 (一对多)
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Order> orders;

    // 一个优惠券属于一个用户（管理员） (多对一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    // 一个优惠券可以被多个用户拥有 (一对多)
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<UserCoupon> userCoupons;

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(String couponNumber) {
        this.couponNumber = couponNumber;
    }

    public Integer getCouponValue() {
        return couponValue;
    }

    public void setCouponValue(Integer couponValue) {
        this.couponValue = couponValue;
    }

    public LocalDateTime getCouponStartTime() {
        return couponStartTime;
    }

    public void setCouponStartTime(LocalDateTime couponStartTime) {
        this.couponStartTime = couponStartTime;
    }

    public LocalDateTime getCouponEndTime() {
        return couponEndTime;
    }

    public void setCouponEndTime(LocalDateTime couponEndTime) {
        this.couponEndTime = couponEndTime;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserCoupon> getUserCoupons() {
        return userCoupons;
    }

    public void setUserCoupons(List<UserCoupon> userCoupons) {
        this.userCoupons = userCoupons;
    }

}
