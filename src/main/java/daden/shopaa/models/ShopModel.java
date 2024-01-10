package daden.shopaa.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import daden.shopaa.utils._enum.*;

import lombok.Builder;
import lombok.Data;

@Document(collection = "Shops")
@Builder
@Data
public class ShopModel implements UserDetails {
  @Id
  private String Id;

  private String name;

  @Indexed(unique = true)
  private String email;

  private String password;

  @Builder.Default
  private List<RoleShopEnum> roles = Arrays.asList(RoleShopEnum.USER);

  @Builder.Default
  private Boolean status = true;

  @Builder.Default
  private Boolean verify = false;

  @Builder.Default
  private AuthTypeEnum authType = AuthTypeEnum.LOCAL;

  @Builder.Default
  private String googleId = null;

  @Builder.Default
  private String facebookId = null;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.getRoles().stream().map(v -> new SimpleGrantedAuthority(v.name())).toList();
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.getEmail();

  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
