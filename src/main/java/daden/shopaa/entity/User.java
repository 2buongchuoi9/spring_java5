package daden.shopaa.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import daden.shopaa.utils._enum.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
  @Id
  private String id;

  private String name;

  @Indexed(unique = true)
  private String email;

  private String image;

  @JsonIgnore
  private String password;

  @Builder.Default
  private Set<RoleShopEnum> roles = Set.of(RoleShopEnum.USER);

  @Builder.Default
  private Boolean status = true;

  @Builder.Default
  private Boolean verify = false;

  @Builder.Default
  private AuthTypeEnum authType = AuthTypeEnum.LOCAL;

  @Builder.Default
  private String oAuth2Id = null;

}
