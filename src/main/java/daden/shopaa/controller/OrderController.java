package daden.shopaa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import daden.shopaa.dto.model.CheckoutModel;
import daden.shopaa.dto.model.MainResponse;
import daden.shopaa.dto.req.CheckoutReq;
import daden.shopaa.entity.Order;
import daden.shopaa.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
  private final OrderService orderService;

  @PostMapping("/checkout-review")
  public ResponseEntity<MainResponse<CheckoutModel>> checkoutReview(@RequestBody @Valid CheckoutReq checkoutReq) {
    return ResponseEntity.ok().body(MainResponse.oke(orderService.checkoutReview(checkoutReq)));
  }

  @PostMapping("/order-user")
  public ResponseEntity<MainResponse<Order>> orderByUser(@RequestBody @Valid CheckoutReq checkoutReq) {
    return ResponseEntity.ok().body(MainResponse.oke(orderService.orderByUser(checkoutReq)));
  }
}
