package daden.shopaa.controller;

import daden.shopaa.dto.model.MainResponse;
import daden.shopaa.dto.parampetterRequest.ProductParamRequets;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;

import daden.shopaa.dto.req.ProductReq;
import daden.shopaa.entity.Product;
import daden.shopaa.repository.repositoryUtils.PageCustom;
import daden.shopaa.services.ProductService;
import daden.shopaa.utils.Constans;
import daden.shopaa.utils.Constans.HASROLE;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
@Tag(name = "product")
public class ProductController {
  private final ProductService productService;

  @Operation(summary = "add product")
  @PreAuthorize(HASROLE.ADMIN)
  @PostMapping("")
  public ResponseEntity<Product> addProduct(@RequestBody ProductReq productReq) {
    return ResponseEntity.ok().body(productService.add(productReq));
  }

  @Operation(summary = "update product")
  @PreAuthorize(HASROLE.ADMIN)
  @PostMapping("/{id}")
  public ResponseEntity<Product> updateProduct(@PathVariable String id, @Valid @RequestBody ProductReq productReq) {
    return ResponseEntity.ok().body(productService.add(productReq));
  }

  @Operation(summary = "find all")
  @GetMapping("")
  public ResponseEntity<MainResponse<PageCustom<Product>>> findAll(
      @PageableDefault(size = 10, page = 0) Pageable pageable,
      @Valid @ModelAttribute ProductParamRequets productParamRequets) {
    return ResponseEntity.ok().body(
        MainResponse.oke(productParamRequets.getMessage(), productService.findAll(pageable, productParamRequets)));
  }

}
