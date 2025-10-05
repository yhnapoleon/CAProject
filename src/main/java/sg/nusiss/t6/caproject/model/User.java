package sg.nusiss.t6.caproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_phone", length = 20, nullable = false, unique = true)
    private String userPhone;

    @Column(name = "user_email", length = 50, nullable = false, unique = true)
    private String userEmail;

    @Column(name = "user_password", length = 100, nullable = false)
    private String userPassword;

    @Column(name = "user_type", nullable = false)
    private Integer userType; // 0=管理员, 1=用户

    @Column(name = "user_register_time", nullable = false)
    private LocalDateTime userRegisterTime;

    @Column(name = "user_lastlogin_time", nullable = false)
    private LocalDateTime userLastLoginTime;

    @Column(name = "user_name", length = 50, nullable = false)
    private String userName;

    @Column(name = "user_gender", length = 50, nullable = false)
    private String userGender;

    @Column(name = "user_birthday")
    private LocalDate userBirthday;

    @Column(name = "user_introduce", columnDefinition = "TEXT")
    private String userIntroduce;

    @Column(name = "user_profile_url", length = 255)
    private String userProfileUrl;

    // 一个用户可以有多个订单 (一对多)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    // 一个用户可以发表多个评论 (一对多)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    // 一个用户（管理员）可以管理多个商品
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    // 一个用户（管理员）可以管理多个优惠券
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coupon> coupons;

    // 一个用户（管理员）可以管理多个折扣活动
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discount> discounts;

    // 一个用户可以有多个购物车项 (一对多)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingCart> shoppingCarts;

    // 一个用户可以拥有多张优惠券 (一对多)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCoupon> userCoupons;


}