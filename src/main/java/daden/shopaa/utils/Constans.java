package daden.shopaa.utils;

public final class Constans {
  public static final String API_V1 = "/api/v1";
  public static final String URL_REDIRECT_OAUTH2_CLIENT = "redirect_url";
  public static final String OAUTH_COOKIE_NAME = "oauth2_auth_request";

  public final class FREE_REQUEST {
    public static final String PRODUCTS = API_V1 + "/products",
        ALL = API_V1 + "/",
        AUTH = API_V1 + "/auth",
        CATE = API_V1 + "/category";
    public static final String[] LIST = {
        "/favicon.ico",
        "/api/v1/cart/add-to-cart",
        PRODUCTS,
        AUTH,
    };
  }

  public final class HEADER {
    public static final String X_CLIENT_ID = "x-client-id";
    public static final String AUTHORIZATION = "authorization";
    public static final String REFRESHTOKEN = "x-rtoken-id";
  }

  public final class HASROLE {
    public static final String ADMIN = "hasAuthority('ADMIN')";
    public static final String USER = "hasAuthority('USER')";
    public static final String MOD = "hasAuthority('MOD')";
  }

}
