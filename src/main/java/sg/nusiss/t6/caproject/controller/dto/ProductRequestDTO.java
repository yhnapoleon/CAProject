package sg.nusiss.t6.caproject.controller.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequestDTO {

  private String productName;

  private String productDescription;

  private BigDecimal productPrice;

  private Integer productStockQuantity;

  private String productCategory;

  private Integer isVisible;

  private String imageUrl;

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductDescription() {
    return productDescription;
  }

  public void setProductDescription(String productDescription) {
    this.productDescription = productDescription;
  }

  public BigDecimal getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(BigDecimal productPrice) {
    this.productPrice = productPrice;
  }

  public Integer getProductStockQuantity() {
    return productStockQuantity;
  }

  public void setProductStockQuantity(Integer productStockQuantity) {
    this.productStockQuantity = productStockQuantity;
  }

  public String getProductCategory() {
    return productCategory;
  }

  public void setProductCategory(String productCategory) {
    this.productCategory = productCategory;
  }

  public Integer getIsVisible() {
    return isVisible;
  }

  public void setIsVisible(Integer isVisible) {
    this.isVisible = isVisible;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
