package daden.shopaa.res;

import daden.shopaa.models.ShopModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
public class LoginRes {
  private TokenStore Token;
  private ShopModel shop;

  /**
   * TokenStore
   */
  @AllArgsConstructor
  @Getter
  public static class TokenStore {
    private String accessToken;
    private String refreshToken;

  }
}
