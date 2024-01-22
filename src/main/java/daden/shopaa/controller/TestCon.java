package daden.shopaa.controller;

import org.springframework.web.bind.annotation.RestController;

import daden.shopaa.entity.Category;
import daden.shopaa.entity.User;
import daden.shopaa.repository.CategoryRepo;
import daden.shopaa.repository.UserRepo;
import daden.shopaa.repository.TestRepo;
import daden.shopaa.utils._enum.RoleShopEnum;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TestCon {
  @Autowired
  UserRepo userRepo;
  @Autowired
  CategoryRepo categoryRepo;

  @GetMapping("/test")
  public Object getMethodName() {
    // ShopModel t = ShopModel.builder().name("root")
    // .email("root@gmail.com")
    // .password("123")
    // .roles(Arrays.asList(RoleShopEnum.ADMIN))
    // .build();

    // shopRepo.insert(t);

    return userRepo.findAll();
  }

  @GetMapping("/test-1")
  public Object testA() {

    Category categoryModel = Category.builder()
        .title("title")
        .description("des")
        .build();

    categoryRepo.save(categoryModel);

    return categoryRepo.findAll();
  }

}
