//By Zhao Jiayi

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
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "order_time", nullable = false, updatable = false)
    private LocalDateTime orderTime;

    @Column(name = "order_status", nullable = false)
    private Integer orderStatus;

    @Column(name = "delivery_type", nullable = false)
    private Integer deliveryType;

    @Column(name = "delivery_location", nullable = false)
    private String deliveryLocation;

    // An order contains many order items (one-to-many)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems;

    // An order belongs to one user (many-to-one)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    // An order can use one discount (many-to-one)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    @JsonBackReference
    private Discount discount;

    // An order can use one coupon (many-to-one)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    @JsonBackReference
    private Coupon coupon;

    @PrePersist
    public void prePersist() {
        if (this.orderTime == null) {
            this.orderTime = LocalDateTime.now();
        }
    }
}