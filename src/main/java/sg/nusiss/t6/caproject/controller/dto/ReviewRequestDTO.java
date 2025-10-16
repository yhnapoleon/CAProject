//By Ying Hao

package sg.nusiss.t6.caproject.controller.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ReviewRequestDTO {

  private String title;

  private String comment;

  private Integer reviewRank;

}
