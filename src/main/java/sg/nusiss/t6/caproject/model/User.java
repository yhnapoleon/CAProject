package sg.nusiss.t6.caproject.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //

    @Column(unique = true, nullable = false)
    private String username; //

    @Column(nullable = false)
    private String password; //

    @Column(unique = true, nullable = false)
    private String email; //

    @Column(nullable = false)
    private String role; //

    @Column(length = 1000)
    private String personalDetails; //

    // 一个用户拥有一个购物车 (一对一)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private ShoppingCart shoppingCart; //

    // 一个用户可以有多个订单 (一对多)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders; //

    // 一个用户可以发表多个评论 (一对多)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews; //

    // (可选) 一个用户可以有多张优惠券
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCoupon> userCoupons; //
}