package daden.shopaa.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import daden.shopaa.dto.model.CartModel;
import daden.shopaa.dto.req.CartProductReq;
import daden.shopaa.dto.req.CartReq;
import daden.shopaa.entity.Cart;
import daden.shopaa.entity.ProductVariation;
import daden.shopaa.entity.User;
import daden.shopaa.exceptions.BabRequestError;
import daden.shopaa.exceptions.NotFoundError;
import daden.shopaa.repository.CartRepo;
import daden.shopaa.repository.ProductRepo;
import daden.shopaa.repository.ProductVariationRepo;
import daden.shopaa.repository.UserRepo;
import daden.shopaa.utils._enum.StateCartEnum;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class CartService {
  private final CartRepo cartRepo;
  private final ProductRepo productRepo;
  private final ProductVariationRepo variationRepo;
  private final UserRepo userRepo;

  // item:[{productId,quantity,price,oldQuantity}],
  // userId:""
  public Cart addToCart(CartReq cartReq) {
    if (!userRepo.existsById(cartReq.getUserId()))
      throw new NotFoundError("userId", cartReq.getUserId());

    Optional<Cart> optionalCart = cartRepo.findByUserId(cartReq.getUserId());

    List<CartModel> listItems = checkoutProductServer(cartReq.getItems());

    if (optionalCart.isPresent()) {
      Cart cart = optionalCart.get();
      cart.setItems(listItems);
      cart.setUserId(cartReq.getUserId());
      return cartRepo.save(cart);
    }

    return cartRepo.save(Cart.builder()
        .items(listItems)
        .state(StateCartEnum.ACTIVE.name())
        .userId(cartReq.getUserId())
        .build());
  }

  public Cart findCartByUserId(String userId) {
    userRepo.findById(userId).orElseThrow(() -> new NotFoundError("userId", userId));

    Cart cart = cartRepo.findByUserIdAndState(userId, StateCartEnum.ACTIVE.name())
        .orElse(Cart.builder()
            .userId(userId)
            .items(new ArrayList<>())
            .build());
    return cartRepo.save(cart);
  }

  public List<CartModel> checkoutProductServer(List<CartProductReq> items) {
    return items.stream()
        .map(v -> {
          Optional<ProductVariation> optional = variationRepo.findById(v.getProductVariationId());
          if (optional.isEmpty())
            throw new NotFoundError("productVariation id", v.getProductVariationId());

          Integer quantity = v.getQuantity() - v.getOldQuantity();
          Double price = productRepo.findById(optional.get().getProductId()).get().getPrice();

          if (optional.get().getQuantity() < quantity)
            throw new BabRequestError(
                "quantity's productVariationId " + optional.get().getId() + " in invertory is "
                    + optional.get().getQuantity() + " ,quantity must less than it");

          return CartModel.builder()
              .productVariationId(v.getProductVariationId())
              .quantity(quantity)
              .price(price)
              .build();
        })
        .filter(v -> v.getQuantity() != 0)
        .collect(Collectors.toList());
  }

  public void removeItemToCart(String userId, String productVariationId) {
    Cart foundCart = cartRepo.findByUserId(userId).orElseThrow(() -> new NotFoundError("userId", userId));
    List<CartModel> newItems = foundCart.getItems().stream()
        .filter(v -> !v.getProductVariationId().equals(productVariationId))
        .collect(Collectors.toList());
    foundCart.setItems(newItems);
    cartRepo.save(foundCart);
  }
}
