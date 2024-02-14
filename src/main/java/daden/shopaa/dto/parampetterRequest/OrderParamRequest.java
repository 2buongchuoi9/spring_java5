package daden.shopaa.dto.parampetterRequest;

import lombok.Data;

@Data
public class OrderParamRequest {
  private String keySearch;

  private Double minTotalOrder;
  private Double maxTotalOrder;

  private Double minTotalCheckout;
  private Double maxTotalCheckout;

  private Double minTotalShopping;
  private Double maxTotalShopping;

  private Double minTotalDiscount;
  private Double maxTotalDiscount;

  private String state;
  private String payment;
}
