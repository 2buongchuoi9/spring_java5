package daden.shopaa.dto.res;

import daden.shopaa.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
public class LoginRes {
  private TokenStore Token;
  private User user;

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
