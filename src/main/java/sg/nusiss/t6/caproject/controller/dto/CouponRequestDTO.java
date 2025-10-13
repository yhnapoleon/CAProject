package sg.nusiss.t6.caproject.controller.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CouponRequestDTO {

  private String couponNumber;

  private Integer couponValue;

  private LocalDateTime couponStartTime;

  private LocalDateTime couponEndTime;

  private String couponName;
}
