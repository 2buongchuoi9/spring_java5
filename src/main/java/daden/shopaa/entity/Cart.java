package daden.shopaa.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import daden.shopaa.dto.CartModel;
import daden.shopaa.utils._enum.StateCartEnum;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Document(collection = "Carts")
@Data
@Builder
public class Cart {
  @Id
  private String id;

  private String userId;

  @Default
  private List<CartModel> products = null;

  @Default
  private String state = StateCartEnum.ACTIVE.name();

}
