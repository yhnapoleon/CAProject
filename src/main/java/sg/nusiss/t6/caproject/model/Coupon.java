//By Zhao Jiayi
//This feature has not been implemented yet.

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

    // A coupon can be used by many orders (one-to-many)
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Order> orders;

    // A coupon belongs to one user (admin) (many-to-one)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    // A coupon can be owned by many users (one-to-many)
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<UserCoupon> userCoupons;
}
