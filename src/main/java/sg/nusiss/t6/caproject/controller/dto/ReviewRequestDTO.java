package sg.nusiss.t6.caproject.controller.dto;

import lombok.Data;

@Data
public class ReviewRequestDTO {

  private String title;

  private String comment;

  private Integer reviewRank;

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

  public Integer getReviewRank() {
    return reviewRank;
  }

  public void setReviewRank(Integer reviewRank) {
    this.reviewRank = reviewRank;
  }
}
