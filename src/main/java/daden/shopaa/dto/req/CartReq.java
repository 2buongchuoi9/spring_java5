package daden.shopaa.dto.req;

import java.util.List;

import daden.shopaa.dto.model.CartModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartReq {
  @NotEmpty(message = "userId not null")
  private String userId;

  private @Valid List<CartProductReq> items;

}
