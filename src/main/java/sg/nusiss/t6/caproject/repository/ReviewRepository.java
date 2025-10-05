package sg.nusiss.t6.caproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.nusiss.t6.caproject.model.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * 根据商品ID查找所有评论（关联字段为 product.productId）
     */
    List<Review> findByProductProductId(Long productId);
}