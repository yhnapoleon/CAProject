package sg.nusiss.t6.caproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;


    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "review_create_time", nullable = false, updatable = false)
    private LocalDateTime reviewCreateTime;

    @Column(name = "review_rank", nullable = false)
    private Integer reviewRank;

    // 一个评论属于一个用户 (多对一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 一个评论属于一个商品 (多对一)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @PrePersist
    public void prePersist() {
        if (this.reviewCreateTime == null) {
            this.reviewCreateTime = LocalDateTime.now();
        }
    }

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getReviewCreateTime() {
        return reviewCreateTime;
    }

    public void setReviewCreateTime(LocalDateTime reviewCreateTime) {
        this.reviewCreateTime = reviewCreateTime;
    }

    public Integer getReviewRank() {
        return reviewRank;
    }

    public void setReviewRank(Integer reviewRank) {
        this.reviewRank = reviewRank;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}