package sg.nusiss.t6.caproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sg.nusiss.t6.caproject.model.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {


    List<Review> findByProductProductId(Long productId);
    

    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.product.productId = :productId")
    List<Review> findByProductProductIdWithUser(@Param("productId") Long productId);
}