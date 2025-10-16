//By Ying Hao

package sg.nusiss.t6.caproject.service;

public interface ReviewService {
    void deleteReview(Integer reviewId);

    /**
     * Delete a review by product and review IDs.
     * 
     * @return true if deleted; false if the review does not exist or does not
     *         belong to the product
     */
    boolean deleteReviewByProduct(Integer productId, Integer reviewId);
}
