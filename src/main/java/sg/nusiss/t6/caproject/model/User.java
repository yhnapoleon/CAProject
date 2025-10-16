//By Xu Wenzhe

package sg.nusiss.t6.caproject.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
    private Integer userType; // 0=ADMIN, 1=USER

    @Column(name = "user_register_time", nullable = false)
    private LocalDateTime userRegisterTime;

    @Column(name = "user_lastlogin_time", nullable = false)
    private LocalDateTime userLastLoginTime;

    @Column(name = "user_name", length = 50, nullable = false, unique = true)
    private String userName;

    @Column(name = "user_gender", length = 50, nullable = false)
    private String userGender;

    @Column(name = "user_birthday")
    private LocalDate userBirthday;

    @Getter
    @Column(name = "user_introduce", columnDefinition = "TEXT")
    private String userIntroduce;

    @Column(name = "user_profile_url")
    private String userProfileUrl;

    @Column(name = "wallet", nullable = false, precision = 10, scale = 2)
    private BigDecimal wallet;

    // One user can have many orders (one-to-many)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Order> orders;

    // One user can create many reviews (one-to-many)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Review> reviews;

    // One user (admin) can manage many products
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Product> products;

    // One user (admin) can manage many coupons
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Coupon> coupons;

    // One user (admin) can manage many discounts
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Discount> discounts;

    // One user can have many shopping cart items (one-to-many)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ShoppingCart> shoppingCarts;

    // One user can have many user coupons (one-to-many)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<UserCoupon> userCoupons;

    // One user can have many addresses (one-to-many)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Location> locations;
}