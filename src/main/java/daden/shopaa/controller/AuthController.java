package daden.shopaa.controller;

import java.security.KeyPair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import daden.shopaa.Auth.JwtService;
import daden.shopaa.models.KeyTokenModel;
import daden.shopaa.repository.KeyTokenRepo;
import daden.shopaa.req.LoginReq;
import daden.shopaa.req.RegisterReq;
import daden.shopaa.services.KeyTokenService;
import daden.shopaa.services.ShopService;
import daden.shopaa.utils.Constans;
import daden.shopaa.utils.Constans.HEADER;
import daden.shopaa.utils.Constans.FREE_REQUEST;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(FREE_REQUEST.AUTH)
public class AuthController {

  @Autowired
  JwtService jwtService;
  @Autowired
  KeyTokenService keyTokenService;
  @Autowired
  ShopService shopService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginReq loginReq) {
    return ResponseEntity.ok().body(shopService.loginLocal(loginReq));
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterReq registerReq) {
    return ResponseEntity.ok().body(shopService.registerLocal(registerReq));
  }
}
