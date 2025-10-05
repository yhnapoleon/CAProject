package sg.nusiss.t6.caproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column(length = 50, nullable = false)
    private String productName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String productDescription;

    @Column(nullable = false)
    private Float productPrice;

    @Column(name = "product_stock_quantity", nullable = false)
    private Integer productStockQuantity;

    @Column(name = "product_category", length = 20, nullable = false)
    private String productCategory;

    @Column(nullable = false)
    private Integer isVisible; // ✅ 对应数据库 int 类型，不用 boolean

    @Column(length = 255)
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingCart> shoppingCarts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



}