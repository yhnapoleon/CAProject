package sg.nusiss.t6.caproject.service;

public interface ReviewService {
    void deleteReview(Integer reviewId);

    /**
     * 根据商品ID与评论ID删除评论。
     * @return true 表示已删除；false 表示评论不存在或不属于该商品
     */
    boolean deleteReviewByProduct(Integer productId, Integer reviewId);
}
