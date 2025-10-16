//By Zhao Jiayi

package sg.nusiss.t6.caproject.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Column(name = "title", nullable = false)
    private String title;

    // A review belongs to one user (many-to-one)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    // A review belongs to one product (many-to-one)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private Product product;

    @PrePersist
    public void prePersist() {
        if (this.reviewCreateTime == null) {
            this.reviewCreateTime = LocalDateTime.now();
        }
    }
}