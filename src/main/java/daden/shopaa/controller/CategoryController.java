package daden.shopaa.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import daden.shopaa.dto.MainResponse;
import daden.shopaa.dto.req.CategoryReq;
import daden.shopaa.entity.Category;
import daden.shopaa.services.CategoryService;
import daden.shopaa.utils.Constans.FREE_REQUEST;
import daden.shopaa.utils.Constans.HASROLE;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
// /api/v1/category
@RequestMapping(FREE_REQUEST.CATE)
@Tag(name = "category")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @Operation(summary = "add category")
  @PreAuthorize(HASROLE.ADMIN)
  @PostMapping("")
  public ResponseEntity<Category> add(@RequestBody CategoryReq categoryReq) {
    return ResponseEntity.ok().body(categoryService.addCategory(categoryReq));
  }

  @Operation(summary = "update category")
  @PreAuthorize(HASROLE.ADMIN)
  @PostMapping("/{id}")
  public ResponseEntity<MainResponse<Category>> update(@PathVariable String id, @RequestBody CategoryReq categoryReq) {
    return ResponseEntity.ok().body(MainResponse.oke(categoryService.updateCategory(id, categoryReq)));
  }

  @Operation(summary = "get all category")
  @GetMapping("")
  public ResponseEntity<List<Category>> findAll() {
    return ResponseEntity.ok().body(categoryService.findAll());
  }

}
