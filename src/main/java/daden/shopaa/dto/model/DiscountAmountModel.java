package daden.shopaa.dto.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DiscountAmountModel {
  private Double totalOrder;
  private Double discount;
  private Double totalPrice;
}
