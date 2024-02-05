package daden.shopaa.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import daden.shopaa.dto.model.CheckoutModel;
import daden.shopaa.dto.model.MainResponse;
import daden.shopaa.dto.req.CartReq;
import daden.shopaa.dto.req.CheckoutReq;
import daden.shopaa.entity.Cart;
import daden.shopaa.entity.Order;
import daden.shopaa.services.CartService;
import daden.shopaa.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {
  private final CartService cartService;

  @GetMapping("/{userId}")
  public ResponseEntity<MainResponse<Cart>> findAll(@PathVariable String userId) {
    return ResponseEntity.ok().body(MainResponse.oke(cartService.findCartByUserId(userId)));
  }

  @PostMapping("/add-to-cart")
  public ResponseEntity<MainResponse<Cart>> addToCart(@RequestBody @Valid CartReq cartReq) {
    return ResponseEntity.ok().body(MainResponse.oke(cartService.addToCart(cartReq)));
  }

}
