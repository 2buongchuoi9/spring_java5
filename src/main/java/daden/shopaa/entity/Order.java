package daden.shopaa.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import daden.shopaa.dto.CartModel;
import daden.shopaa.utils._enum.StateOrderEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Document(collection = "Orders")
@Data
@Builder
public class Order {
  @Id
  private String id;

  private String userId;

  private String shipping; // address shipping

  private Double totalShipping;

  private String payment;

  private List<CartModel> products;

  @Default
  private String state = StateOrderEnum.PENDING.name();

}
