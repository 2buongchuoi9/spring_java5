package daden.shopaa.controller;

import org.springframework.web.bind.annotation.RestController;

import daden.shopaa.models.ShopModel;
import daden.shopaa.repository.ShopRepo;
import daden.shopaa.repository.TestRepo;
import daden.shopaa.utils._enum.RoleShopEnum;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TestCon {
  @Autowired
  ShopRepo shopRepo;

  @GetMapping("/test")
  public Object getMethodName() {
    // ShopModel t = ShopModel.builder().name("root")
    // .email("root@gmail.com")
    // .password("123")
    // .roles(Arrays.asList(RoleShopEnum.ADMIN))
    // .build();

    // shopRepo.insert(t);

    return shopRepo.findAll();
  }

}
