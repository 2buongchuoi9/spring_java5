package daden.shopaa.services;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import daden.shopaa.dto.model.CartModel;
import daden.shopaa.dto.model.CheckoutModel;
import daden.shopaa.dto.req.CartProductReq;
import daden.shopaa.dto.req.CheckoutReq;
import daden.shopaa.entity.Cart;
import daden.shopaa.entity.Order;
import daden.shopaa.exceptions.BabRequestError;
import daden.shopaa.repository.CartRepo;
import daden.shopaa.repository.OrderRepo;
import daden.shopaa.repository.ProductVariationRepo;
import daden.shopaa.repository.UserRepo;
import daden.shopaa.utils._enum.StateOrderEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class OrderService {
  private final OrderRepo orderRepo;
  private final ProductvariationService variationService;
  private final CartService cartService;
  private final DiscountService discountService;
  private final UserRepo userRepo;
  private final CartRepo cartRepo;

  // cartId:""
  // userId:""
  // items:[{productVariationId,quantity,oldQuantity}],
  public CheckoutModel checkoutReview(CheckoutReq checkoutReq) {
    String cartId = checkoutReq.getCartId();
    String discountId = checkoutReq.getDiscountId();
    String userId = checkoutReq.getUserId();
    List<CartProductReq> items = checkoutReq.getItems();

    cartRepo.findById(cartId)
        .orElseThrow(() -> new BabRequestError("not found cart id: " + cartId));

    userRepo.findById(userId)
        .orElseThrow(() -> new BabRequestError("not found user id: " + cartId));

    List<CartModel> listItems = cartService.checkoutProductServer(items);

    // tinh tong tien don hang chua xu li
    Double totalOrder = listItems.stream()
        .mapToDouble(e -> e.getPrice() * e.getQuantity())
        .sum();

    // check discount and calculate totalPrice after aplyy discount
    Double[] resultDiscount = { 0.0, totalOrder };
    if (discountId != null && !discountId.isEmpty())
      resultDiscount = discountService.getDiscountAmount(items, discountId, userId);

    double totalShip = Math.ceil(Math.random() * 16 + 5) * 1000;

    return CheckoutModel.builder()
        .userId(userId)
        .cartId(cartId)
        .address(checkoutReq.getAddress())
        .discountId(discountId)
        .items_checkout(listItems)
        .totalOrder(totalOrder)
        .totalDiscount(resultDiscount[0])
        .totaShip(totalShip) // randome 5000 -> 20000
        .totalCheckout(resultDiscount[1] + totalShip)
        .payment(checkoutReq.getPayment())
        .build();
  }

  @Transactional(rollbackFor = Exception.class)
  public Order orderByUser(CheckoutReq checkoutReq) {
    CheckoutModel checkout = checkoutReview(checkoutReq);

    try {
      // save order
      Order order = orderRepo.save(
          Order.builder()
              .userId(checkout.getUserId())
              .shippingAddress(checkout.getAddress())
              .totalOrder(checkout.getTotalOrder())
              .totalShipping(checkout.getTotaShip())
              .totalDiscount(checkout.getTotalDiscount())
              .totalCheckout(checkout.getTotalCheckout())
              .items(checkout.getItems_checkout())
              .state(StateOrderEnum.PENDING.name())
              .build());

      // remove product items in cart and set quantity in productVariation
      order.getItems().stream()
          .forEach(v -> {
            cartService.removeItemToCart(order.getUserId(), v.getProductVariationId());
            variationService.updateQuantityProductVariation(v.getQuantity(), v.getProductVariationId());
          });
      return order;

    } catch (Exception e) {
      throw new BabRequestError("fali to save order. Error at: " + e.getMessage());
    }

  }

}
