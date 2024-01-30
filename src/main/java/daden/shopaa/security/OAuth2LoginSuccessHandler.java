package daden.shopaa.security;

import java.io.IOException;
import java.security.KeyPair;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import daden.shopaa.dto.res.LoginRes.TokenStore;
import daden.shopaa.entity.User;
import daden.shopaa.security.jwt.JwtService;
import daden.shopaa.services.KeyTokenService;
import daden.shopaa.utils.Constans;
import daden.shopaa.utils.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  @Value("${den.url_client}")
  private String url_client;

  final JwtService jwtService;
  final KeyTokenService keyTokenService;

  public static final String OAUTH_COOKIE = Constans.OAUTH_COOKIE_NAME;
  public static final String REDIRECT_URL = Constans.URL_REDIRECT_OAUTH2_CLIENT;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    String targetUrl = ggggg(request, response, authentication);

    System.out.println("url:::::" + targetUrl);

    // if (response.isCommitted())
    // return;

    // clearAuthenticationAttributes(request, response);
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }

  private TokenStore loginWithOAuth2(User user) {

    KeyPair keys = JwtService.generatorKeyPair();

    TokenStore tokens = new TokenStore(jwtService.createAccessToken(user.getEmail(), keys.getPrivate()),
        jwtService.createRefreshToken(user.getEmail(), keys.getPrivate()));

    if (!keyTokenService.createKeyStore(
        user.getId(),
        jwtService.getStringFromPublicKey(keys.getPublic()),
        tokens.getRefreshToken()))
      throw new RuntimeException("fail to create keyStore");

    return tokens;
  }

  private String ggggg(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    Optional<String> redirectUri = CookieUtils
        .getCookie(request, Constans.URL_REDIRECT_OAUTH2_CLIENT)
        .map(Cookie::getValue);

    String targetUrl = redirectUri.orElse(url_client);
    System.out.println("redirect::::::::::" + redirectUri);

    User user = ((UserRoot) authentication.getPrincipal()).getUser();
    TokenStore tokenStore = loginWithOAuth2(user);

    return UriComponentsBuilder.fromUriString(targetUrl)
        .queryParam("userId", user.getId())
        .queryParam("accessToken", tokenStore.getAccessToken())
        .queryParam("refreshToken", tokenStore.getRefreshToken())
        .build().toUriString();
  }

}
