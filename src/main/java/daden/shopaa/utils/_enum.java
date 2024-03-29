package daden.shopaa.utils;

public final class _enum {
  public static enum RoleShopEnum {
    ADMIN,
    SHOP,
    USER,
    MOD;
  }

  public static enum AuthTypeEnum {
    LOCAL,
    FACEBOOK,
    GOOGLE,
  }

  public enum StateCartEnum {
    ACTIVE,
    COMPLETED,
    FAILED,
    PENDING,

  }

  public enum StateOrderEnum {
    PENDING,
    CONFIRMED,
    SHIPPING,
    CANCELLED,
    DELIVERED,
  }

  public enum StatusProductEnum {
    DRAFT, PUBLISHED
  }

  public enum TypeDiscount {
    FIXED_AMOUNT, PERCENTAGE_AMOUNT
  }

  public enum OrderStateEnum {
    PENDING,
    CONFIRMED,
    SHIPPED,
    CANCELLED,
    DELIVERED,
  }

  public enum TypePayment {
    CASH,
    MOMO,
    CARD_BANK,
  }

}