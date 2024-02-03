package daden.shopaa.controller;

import daden.shopaa.dto.PageCustom;
import daden.shopaa.dto.parampetterRequest.ProductParamRequets;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;

import daden.shopaa.dto.req.ProductReq;
import daden.shopaa.entity.Product;
import daden.shopaa.services.ProductService;
import daden.shopaa.utils.Constans;
import daden.shopaa.utils.Constans.HASROLE;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

  // @Operation(summary = "asjhdkasj")
  // @GetMapping("/")
  // public ResponseEntity<?> findAllProduct(
  // @PageableDefault(page = 0, size = 20) Pageable pageable,
  // @RequestParam(required = false) String keySearch,
  // @RequestParam(required = false) Double minPrice,
  // @RequestParam(required = false) Double maxPrice,
  // @RequestParam(required = false) Boolean status,

  // @RequestBody ProductReq productReq) {
  // return ResponseEntity.ok().body(null);
  // }

  @Operation(summary = "add product")
  @PreAuthorize(HASROLE.ADMIN)
  @PostMapping("")
  public ResponseEntity<Product> addProduct(@RequestBody ProductReq productReq) {
    return ResponseEntity.ok().body(productService.add(productReq));
  }

  @Operation(summary = "update product")
  @PreAuthorize(HASROLE.ADMIN)
  @PostMapping("/{id}")
  public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody ProductReq productReq) {
    return ResponseEntity.ok().body(productService.add(productReq));
  }

  @Operation(summary = "find all")
  @GetMapping("")
  public ResponseEntity<PageCustom<Product>> findAll(
      @PageableDefault(size = 10, page = 0) Pageable pageable,
      @ModelAttribute ProductParamRequets productParamRequets) {
    return ResponseEntity.ok().body(productService.findAll(pageable, productParamRequets));
  }

}
