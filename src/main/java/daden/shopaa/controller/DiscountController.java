package daden.shopaa.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import daden.shopaa.dto.model.MainResponse;
import daden.shopaa.dto.req.CreateDiscountReq;
import daden.shopaa.entity.Discount;
import daden.shopaa.repository.repositoryUtils.PageCustom;
import daden.shopaa.services.DiscountService;
import daden.shopaa.utils.Constans.HASROLE;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discount")
public class DiscountController {

  private final DiscountService discountService;

  @PostMapping("")
  @PreAuthorize(HASROLE.ADMIN)
  public ResponseEntity<MainResponse<Discount>> addDiscount(@RequestBody @Valid CreateDiscountReq createDiscountReq) {
    return ResponseEntity.ok().body(MainResponse.oke(discountService.createDiscount(createDiscountReq)));
  }

  @GetMapping("")
  // @PreAuthorize(HASROLE.ADMIN)
  public ResponseEntity<MainResponse<PageCustom<Discount>>> findAllDiscount(
      @PageableDefault(page = 0, size = 10) Pageable pageable) {
    return ResponseEntity.ok().body(MainResponse.oke(discountService.findAllDiscount(pageable)));
  }

}
