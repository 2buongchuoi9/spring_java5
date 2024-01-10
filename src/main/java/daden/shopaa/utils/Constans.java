package daden.shopaa.utils;

public final class Constans {
  public static final String API_V1 = "/api/v1";

  public final class FREE_REQUEST {
    public static final String PRODUCTS = API_V1 + "/products",
        ALL = API_V1 + "/",
        AUTH = API_V1 + "/auth";
    public static final String[] LIST = {
        "/favicon.ico",
        PRODUCTS,
        AUTH
    };
  }

  public final class HEADER {
    public static final String X_CLIENT_ID = "x-client-id";
    public static final String AUTHORIZATION = "authorization";
    public static final String REFRESHTOKEN = "x-rtoken-id";
  }
}
