package sg.nusiss.t6.caproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // [cite: 167]

    @Column(nullable = false)
    private LocalDateTime orderDate; // [cite: 168]

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount; // [cite: 169]

    @Column(nullable = false)
    private String status; // [cite: 170]

    // 一个订单属于一个用户 (多对一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user; // [cite: 171]

    // 一个订单包含多个订单项 (一对多)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems; // [cite: 172]

    @PrePersist
    public void prePersist() {
        this.orderDate = LocalDateTime.now();
    }
}