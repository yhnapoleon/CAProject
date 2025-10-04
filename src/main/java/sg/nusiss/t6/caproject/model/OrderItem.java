package sg.nusiss.t6.caproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // [cite: 176]

    @Column(nullable = false)
    private Integer quantity; // [cite: 177]

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice; // [cite: 178]

    // 一个订单项属于一个订单 (多对一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order; // [cite: 179]

    // 一个订单项对应一个商品 (多对一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // [cite: 180]
}