package daden.shopaa.dto.req;

import java.util.List;

import daden.shopaa.utils._enum.TypePayment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Data
@Builder
public class CheckoutReq {
  @NotEmpty(message = "cartId not null")
  private String cartId;
  @NotEmpty(message = "userId not null")
  private String userId;
  @NotEmpty(message = "address not null")
  private String address;
  @Default
  private String payment = TypePayment.CASH.name();
  private String discountId;
  private @Valid List<CartProductReq> items;
}
