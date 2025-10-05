package sg.nusiss.t6.caproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@Entity
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "order_time", nullable = false, updatable = false)
    private LocalDateTime orderTime;

    @Column(name = "order_status", nullable = false)
    private Integer orderStatus;

    // 一个订单包含多个订单项 (一对多)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    // 一个订单属于一个用户 (多对一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 一个订单可以使用一个折扣 (多对一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private Discount discount;

    // 一个订单可以使用一张优惠券 (多对一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @PrePersist
    public void prePersist() {
        if (this.orderTime == null) {
            this.orderTime = LocalDateTime.now();
        }
    }

}