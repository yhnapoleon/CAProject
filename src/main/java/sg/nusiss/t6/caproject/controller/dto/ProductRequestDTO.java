//By Ying Hao

package sg.nusiss.t6.caproject.controller.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class ProductRequestDTO {

  private String productName;

  private String productDescription;

  private BigDecimal productPrice;

  private Integer productStockQuantity;

  private String productCategory;

  private Integer isVisible;

  private String imageUrl;
}
