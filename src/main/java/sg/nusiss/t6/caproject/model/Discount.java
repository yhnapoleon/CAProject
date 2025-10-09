package sg.nusiss.t6.caproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@Entity
@Table(name = "discount")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Integer discountId;


    @Column(name = "discount_name", length = 100)
    private String discountName;

    @Column(name = "discount_description", columnDefinition = "TEXT")
    private String discountDescription;

    @Column(name = "discount_start_time", nullable = false, updatable = false)
    private LocalDateTime discountStartTime;

    @Column(name = "discount_end_time")
    private LocalDateTime discountEndTime;

    @Column(name = "discount_discount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountDiscount;

    // 一个折扣可以被多个订单使用 (一对多)
    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

    // 一个折扣属于一个用户（管理员） (多对一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        if (this.discountStartTime == null) {
            this.discountStartTime = LocalDateTime.now();
        }
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public String getDiscountDescription() {
        return discountDescription;
    }

    public void setDiscountDescription(String discountDescription) {
        this.discountDescription = discountDescription;
    }

    public LocalDateTime getDiscountStartTime() {
        return discountStartTime;
    }

    public void setDiscountStartTime(LocalDateTime discountStartTime) {
        this.discountStartTime = discountStartTime;
    }

    public LocalDateTime getDiscountEndTime() {
        return discountEndTime;
    }

    public void setDiscountEndTime(LocalDateTime discountEndTime) {
        this.discountEndTime = discountEndTime;
    }

    public BigDecimal getDiscountDiscount() {
        return discountDiscount;
    }

    public void setDiscountDiscount(BigDecimal discountDiscount) {
        this.discountDiscount = discountDiscount;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
