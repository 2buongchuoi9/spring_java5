package daden.shopaa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import daden.shopaa.entity.KeyToken;
import daden.shopaa.entity.User;
import daden.shopaa.exceptions.UnauthorizeError;
import daden.shopaa.repository.KeyTokenRepo;
import daden.shopaa.security.jwt.JwtService;
import daden.shopaa.dto.model.MainResponse;
import daden.shopaa.dto.req.LoginReq;
import daden.shopaa.dto.req.RegisterReq;
import daden.shopaa.dto.res.LoginRes;
import daden.shopaa.services.KeyTokenService;
import daden.shopaa.services.UserService;
import daden.shopaa.utils.Constans.FREE_REQUEST;
import daden.shopaa.utils.Constans.HEADER;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

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
  public ResponseEntity<MainResponse<LoginRes>> login(@RequestBody LoginReq loginReq) {
    return ResponseEntity.ok().body(MainResponse.oke(shopService.loginLocal(loginReq)));
  }

  @PostMapping("/register")
  public ResponseEntity<MainResponse<LoginRes>> register(@RequestBody RegisterReq registerReq) {
    return ResponseEntity.ok().body(MainResponse.oke(shopService.registerLocalv2(registerReq)));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<MainResponse<LoginRes>> refeshToken(HttpServletRequest req) {
    return ResponseEntity.ok().body(MainResponse.oke(shopService.handleRefeshToken(req)));
  }

  @GetMapping("/create-user-mod")
  public ResponseEntity<MainResponse<User>> createUserMod(HttpServletRequest req) {
    String ipAddress = req.getHeader("X-Forwarded-For");
    if (ipAddress == null)
      ipAddress = req.getRemoteAddr();
    return ResponseEntity.ok().body(MainResponse.oke(shopService.createUserMod(ipAddress)));
  }

  @PostMapping("/conver-mod-to-user/{id}")
  public ResponseEntity<MainResponse<LoginRes>> convserModToUser(
      HttpServletRequest req,
      @PathVariable String id,
      @RequestBody @Valid RegisterReq registerReq) {
    String ipAddress = req.getHeader("X-Forwarded-For");
    if (ipAddress == null)
      ipAddress = req.getRemoteAddr();
    return ResponseEntity.ok().body(MainResponse.oke(shopService.converModToUserv2(id, registerReq)));
  }

}
