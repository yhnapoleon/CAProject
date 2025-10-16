//By Ying Hao

package sg.nusiss.t6.caproject.controller.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DiscountRequestDTO {

  private String discountName;

  private String discountDescription;

  private LocalDateTime discountStartTime;

  private LocalDateTime discountEndTime;

  private BigDecimal discountDiscount;

}
