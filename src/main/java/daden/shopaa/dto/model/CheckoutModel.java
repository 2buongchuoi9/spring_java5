package daden.shopaa.dto.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckoutModel {
  private String userId;
  private String cartId;
  private String address;
  private String discountId;
  private List<CartModel> items;
  private String payment;
  private Double totalOrder;
  private Double totaShip;
  private Double totalDiscount;
  private Double totalCheckout;
}
