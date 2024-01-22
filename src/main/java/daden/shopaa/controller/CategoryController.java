package daden.shopaa.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import daden.shopaa.dto.req.CategoryReq;
import daden.shopaa.entity.Category;
import daden.shopaa.services.CategoryService;
import daden.shopaa.utils.Constans.FREE_REQUEST;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(FREE_REQUEST.CATE)
@Tag(name = "category")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @Operation(summary = "add category")
  // @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/")
  public ResponseEntity<Category> postMethodName(@RequestBody CategoryReq categoryReq) {
    return ResponseEntity.ok().body(categoryService.addCategory(categoryReq));
  }

}
