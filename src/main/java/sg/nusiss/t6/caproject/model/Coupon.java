package sg.nusiss.t6.caproject.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "coupons")
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

    @Column(name = "coupon_type", length = 20)
    private String couponType;

    @Column(name = "discount", nullable = false)
    private Float discount;

    @Column(name = "manjian_value", nullable = false)
    private Integer manjianValue;

    @Column(name = "coupon_name", length = 20)
    private String couponName;

    @Column(name = "coupon_quantity", nullable = false)
    private Integer couponQuantity;

    // 一个优惠券可以被多个订单使用 (一对多)
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    // 一个优惠券属于一个用户（管理员） (多对一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 一个优惠券可以被多个用户拥有 (一对多)
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCoupon> userCoupons;
}
