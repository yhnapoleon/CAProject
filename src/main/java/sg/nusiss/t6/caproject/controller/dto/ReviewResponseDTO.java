//By Zhao Jiayi

package sg.nusiss.t6.caproject.controller.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class ReviewResponseDTO {
    private Integer reviewId;
    private String title; // New field: review title
    private String comment;
    private LocalDateTime reviewCreateTime;
    private Integer reviewRank;
    private String userName; // New field: username

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