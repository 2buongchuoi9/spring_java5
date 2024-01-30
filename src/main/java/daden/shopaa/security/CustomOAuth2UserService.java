package daden.shopaa.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import daden.shopaa.entity.User;
import daden.shopaa.repository.UserRepo;
import daden.shopaa.security.oauth2User.OAuth2UserInfo;
import daden.shopaa.utils._enum.AuthTypeEnum;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
  @Autowired
  private UserRepo userRepo;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
    String typeOAuth = oAuth2UserRequest.getClientRegistration().getClientName();

    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.getOAuth2User(
        oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

    System.out.println("::::::::::" + oAuth2User.getAttributes().toString());

    if (oAuth2UserInfo.getEmail() == null && oAuth2UserInfo.getEmail().isEmpty()) {
      throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
    }

    Optional<User> userOptional = userRepo.findByEmail(oAuth2UserInfo.getEmail());
    User user;
    if (userOptional.isPresent()) {
      // update
      user = userOptional.get();
      user.setName(oAuth2UserInfo.getName());
      user.setAuthType(typeOAuth.equals("Google") ? AuthTypeEnum.GOOGLE : AuthTypeEnum.FACEBOOK);
      user.setImage(oAuth2UserInfo.getImageUrl());
      user = userRepo.save(user);
    } else {
      // create
      user = userRepo.save(User.builder()
          .name(oAuth2UserInfo.getName())
          .email(oAuth2UserInfo.getEmail())
          .authType(typeOAuth.equals("Google") ? AuthTypeEnum.GOOGLE : AuthTypeEnum.FACEBOOK)
          .oAuth2Id(oAuth2UserInfo.getId())
          .image(oAuth2UserInfo.getImageUrl())
          .build());
    }

    return UserRoot.create(user, oAuth2UserInfo.getAttributes());
  }

}