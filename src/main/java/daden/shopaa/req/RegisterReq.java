package daden.shopaa.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterReq {
  private String name;
  private String email;
  private String password;
}
