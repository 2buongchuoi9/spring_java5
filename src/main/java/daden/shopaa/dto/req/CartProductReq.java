package daden.shopaa.dto.req;

import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

@Data
public class CartProductReq {
  private String productVariationId;
  private Integer quantity;
  private Integer oldQuantity = 0;

  @AssertTrue(message = "quantity - oldQuantity must >= 0")
  public boolean isValidateQuantity() {
    return quantity - oldQuantity >= 0;
  }
}
