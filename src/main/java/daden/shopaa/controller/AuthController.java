package daden.shopaa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import daden.shopaa.entity.KeyToken;
import daden.shopaa.entity.User;
import daden.shopaa.exceptions.UnauthorizeError;
import daden.shopaa.repository.KeyTokenRepo;
import daden.shopaa.security.jwt.JwtService;
import daden.shopaa.dto.req.LoginReq;
import daden.shopaa.dto.req.RegisterReq;
import daden.shopaa.dto.res.LoginRes;
import daden.shopaa.services.KeyTokenService;
import daden.shopaa.services.UserService;
import daden.shopaa.utils.Constans.FREE_REQUEST;
import daden.shopaa.utils.Constans.HEADER;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(FREE_REQUEST.AUTH)
public class AuthController {

  @Autowired
  JwtService jwtService;
  @Autowired
  KeyTokenService keyTokenService;
  @Autowired
  UserService shopService;

  @PostMapping("/login")
  public ResponseEntity<LoginRes> login(@RequestBody LoginReq loginReq) {
    return ResponseEntity.ok().body(shopService.loginLocal(loginReq));
  }

  @PostMapping("/register")
  public ResponseEntity<User> register(@RequestBody RegisterReq registerReq) {
    return ResponseEntity.ok().body(shopService.registerLocal(registerReq));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<LoginRes> refeshToken(HttpServletRequest req) {
    return ResponseEntity.ok().body(shopService.handleRefeshToken(req));
  }

}
