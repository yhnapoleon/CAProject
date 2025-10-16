package sg.nusiss.t6.caproject.controller.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewResponseDTO {
    private Integer reviewId;
    private String title; // New field: review title
    private String comment;
    private LocalDateTime reviewCreateTime;
    private Integer reviewRank;
    private String userName; // New field: user name

    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ReviewResponseDTO() {
    }

    public ReviewResponseDTO(Integer reviewId, String title, String comment, LocalDateTime reviewCreateTime,
            Integer reviewRank, String userName) {
        this.reviewId = reviewId;
        this.title = title;
        this.comment = comment;
        this.reviewCreateTime = reviewCreateTime;
        this.reviewRank = reviewRank;
        this.userName = userName;
    }
}