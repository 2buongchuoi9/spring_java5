package daden.shopaa.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import daden.shopaa.dto.model.CheckoutModel;
import daden.shopaa.dto.model.MainResponse;
import daden.shopaa.dto.parampetterRequest.OrderParamRequest;
import daden.shopaa.dto.req.CheckoutReq;
import daden.shopaa.dto.req.UpdateStateOrder;
import daden.shopaa.entity.Order;
import daden.shopaa.repository.repositoryUtils.PageCustom;
import daden.shopaa.services.OrderService;
import daden.shopaa.utils.Constans.HASROLE;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;

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

  @PostMapping("")
  @PreAuthorize(HASROLE.ADMIN)
  public ResponseEntity<MainResponse<PageCustom<Order>>> getAll(
      @PageableDefault(page = 0, size = 10) Pageable pageable,
      @ModelAttribute @Valid OrderParamRequest orderParamRequest) {
    return ResponseEntity.ok().body(MainResponse.oke(orderService.findAllOrder(pageable, orderParamRequest)));
  }

  @PostMapping("/{id}")
  @PreAuthorize(HASROLE.ADMIN)
  public ResponseEntity<MainResponse<Order>> getOrderById(@PathVariable String id) {
    return ResponseEntity.ok().body(MainResponse.oke(orderService.findOrderById(id)));
  }

  @PostMapping("/update-state/{id}")
  @PreAuthorize(HASROLE.ADMIN)
  public ResponseEntity<MainResponse<Order>> setStateOrderByAdmin(
      @PathVariable String id,
      @RequestBody @Valid UpdateStateOrder updateStateOrder) {
    return ResponseEntity.ok().body(MainResponse
        .oke(orderService.setStateOrderByAdmin(id, updateStateOrder.getState(), updateStateOrder.getNote())));
  }

  @PostMapping("/cancel/{id}")
  @PreAuthorize(HASROLE.USER + " or " + HASROLE.USER)
  public ResponseEntity<MainResponse<Order>> cancelOrderByUser(
      @PathVariable String id,
      @RequestBody @Valid UpdateStateOrder updateStateOrder) {
    return ResponseEntity.ok().body(MainResponse
        .oke(orderService.cancelOrderByUser(updateStateOrder.getState(), updateStateOrder.getNote())));
  }
}
