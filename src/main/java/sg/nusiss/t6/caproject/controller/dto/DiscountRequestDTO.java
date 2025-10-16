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

  public String getDiscountName() {
    return discountName;
  }

  public void setDiscountName(String discountName) {
    this.discountName = discountName;
  }

  public String getDiscountDescription() {
    return discountDescription;
  }

  public void setDiscountDescription(String discountDescription) {
    this.discountDescription = discountDescription;
  }

  public LocalDateTime getDiscountStartTime() {
    return discountStartTime;
  }

  public void setDiscountStartTime(LocalDateTime discountStartTime) {
    this.discountStartTime = discountStartTime;
  }

  public LocalDateTime getDiscountEndTime() {
    return discountEndTime;
  }

  public void setDiscountEndTime(LocalDateTime discountEndTime) {
    this.discountEndTime = discountEndTime;
  }

  public BigDecimal getDiscountDiscount() {
    return discountDiscount;
  }

  public void setDiscountDiscount(BigDecimal discountDiscount) {
    this.discountDiscount = discountDiscount;
  }
}
