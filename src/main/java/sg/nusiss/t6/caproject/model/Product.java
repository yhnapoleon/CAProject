//By Ying Hao
package sg.nusiss.t6.caproject.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.math.BigDecimal;

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

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal productPrice;

    @Column(name = "product_stock_quantity", nullable = false)
    private Integer productStockQuantity;

    @Column(name = "product_category", length = 20, nullable = false)
    private String productCategory;

    @Column(nullable = false)
    private Integer isVisible; // Corresponds to DB int type; not using boolean

    @Column()
    private String imageUrl;

    @Column(name = "product_brand", length = 50)
    private String productBrand; // New field: brand

    @Column(name = "product_sku", length = 50)
    private String productSku; // New field: SKU

    @Column(name = "product_weight", precision = 10, scale = 2)
    private BigDecimal productWeight; // New field: weight (use BigDecimal for precision)

    @Column(name = "product_dimensions", length = 100)
    private String productDimensions; // New field: dimensions (use String, e.g., "23 x 15 x 10 cm")

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Review> reviews;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ShoppingCart> shoppingCarts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
}