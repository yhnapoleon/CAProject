//By Ying Hao

package sg.nusiss.t6.caproject.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nusiss.t6.caproject.repository.ReviewRepository;
import sg.nusiss.t6.caproject.service.ReviewService;
import sg.nusiss.t6.caproject.model.Review;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional
    public void deleteReview(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Override
    @Transactional
    public boolean deleteReviewByProduct(Integer productId, Integer reviewId) {
        // Load and verify whether the review belongs to the specified product
        java.util.Optional<Review> opt = reviewRepository.findById(reviewId);
        if (opt.isEmpty()) {
            return false;
        }
        Review review = opt.get();
        if (review.getProduct() == null || review.getProduct().getProductId() == null) {
            return false;
        }
        if (!review.getProduct().getProductId().equals(productId)) {
            return false;
        }
        reviewRepository.deleteById(reviewId);
        return true;
    }
}
