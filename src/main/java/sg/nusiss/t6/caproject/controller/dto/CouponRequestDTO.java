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

  public String getCouponNumber() {
    return couponNumber;
  }

  public void setCouponNumber(String couponNumber) {
    this.couponNumber = couponNumber;
  }

  public Integer getCouponValue() {
    return couponValue;
  }

  public void setCouponValue(Integer couponValue) {
    this.couponValue = couponValue;
  }

  public LocalDateTime getCouponStartTime() {
    return couponStartTime;
  }

  public void setCouponStartTime(LocalDateTime couponStartTime) {
    this.couponStartTime = couponStartTime;
  }

  public LocalDateTime getCouponEndTime() {
    return couponEndTime;
  }

  public void setCouponEndTime(LocalDateTime couponEndTime) {
    this.couponEndTime = couponEndTime;
  }

  public String getCouponName() {
    return couponName;
  }

  public void setCouponName(String couponName) {
    this.couponName = couponName;
  }
}
