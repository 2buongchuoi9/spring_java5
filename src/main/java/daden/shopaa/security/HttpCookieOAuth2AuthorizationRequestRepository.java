package daden.shopaa.security;

import java.util.Optional;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import daden.shopaa.utils.Constans;
import daden.shopaa.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository
    implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
  private static final String OAUTH_COOKIE = Constans.OAUTH_COOKIE_NAME;
  private static final String REDIRECT_URL = Constans.URL_REDIRECT_OAUTH2_CLIENT;
  private static final int cookieExpireSeconds = 180;

  @Override
  public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
    // String redirectUriAfterLogin = request.getParameter(REDIRECT_URL);
    System.out.println("load:::" + request.getRequestURI());

    return CookieUtils.getCookie(request, OAUTH_COOKIE)
        .map(cookie -> CookieUtils.deserialize(cookie,
            OAuth2AuthorizationRequest.class))
        .orElse(null);
  }

  @Override
  public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
      HttpServletResponse response) {

    if (authorizationRequest == null) {
      CookieUtils.deleteCookie(request, response, OAUTH_COOKIE);
      CookieUtils.deleteCookie(request, response, REDIRECT_URL);
      return;
    }

    System.out.println("reqURL:::" + authorizationRequest.getAuthorizationRequestUri());
    System.out.println("redirectURL:::" + authorizationRequest.getRedirectUri());

    CookieUtils.addCookie(response, OAUTH_COOKIE,
        CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);
    String redirectUriAfterLogin = request.getParameter(REDIRECT_URL);
    System.out.println("truoc:::" + redirectUriAfterLogin);
    if (StringUtils.hasText(redirectUriAfterLogin)) {
      CookieUtils.addCookie(response, REDIRECT_URL, redirectUriAfterLogin,
          cookieExpireSeconds);
    }

    Optional<Cookie> cookie = CookieUtils.getCookie(request, REDIRECT_URL);
    System.out.println();

    // if (redirectUriAfterLogin != null && !redirectUriAfterLogin.isEmpty()) {
    // CookieUtils.addCookie(response, REDIRECT_URL, redirectUriAfterLogin,
    // cookieExpireSeconds);
    // }

  }

  @Override
  public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
      HttpServletResponse response) {
    OAuth2AuthorizationRequest authorizationRequest = this.loadAuthorizationRequest(request);
    // CookieUtils.deleteCookie(request, response, OAUTH_COOKIE);
    // CookieUtils.deleteCookie(request, response, REDIRECT_URL);
    return authorizationRequest;
  }

  // public void removeAuthorizationRequestCookies(HttpServletRequest request,
  // HttpServletResponse response) {
  // CookieUtils.deleteCookie(request, response, OAUTH_COOKIE);
  // CookieUtils.deleteCookie(request, response, REDIRECT_URL);
  // }
}