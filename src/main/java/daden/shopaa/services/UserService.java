package daden.shopaa.services;

import java.security.KeyPair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import daden.shopaa.Auth.JwtService;
import daden.shopaa.entity.User;
import daden.shopaa.exceptions.BabRequestError;
import daden.shopaa.exceptions.NotFoundException;
import daden.shopaa.repository.UserRepo;
import daden.shopaa.dto.req.LoginReq;
import daden.shopaa.dto.req.RegisterReq;
import daden.shopaa.dto.res.LoginRes;
import daden.shopaa.dto.res.LoginRes.TokenStore;

@Service
public class UserService {
  @Autowired
  private JwtService jwtService;
  @Autowired
  private KeyTokenService keyTokenService;
  @Autowired
  private UserRepo shopRepo;
  @Autowired
  private PasswordEncoder passwordEncoder;

  public LoginRes loginLocal(LoginReq loginReq) {

    // check email password
    User foundShop = shopRepo.findByEmail(loginReq.getEmail())
        .orElseThrow(() -> new NotFoundException("shop is not registered"));

    System.out.println("::::::::::::::::::" + passwordEncoder.encode(loginReq.getPassword()));

    if (!passwordEncoder.matches(loginReq.getPassword(), foundShop.getPassword()))
      throw new BabRequestError("password is not true");

    KeyPair keys = JwtService.generatorKeyPair();

    TokenStore tokens = new TokenStore(jwtService.createAccessToken(loginReq.getEmail(), keys.getPrivate()),
        jwtService.createRefreshToken(loginReq.getEmail(), keys.getPrivate()));

    if (!keyTokenService.createKeyStore(
        foundShop.getId(),
        jwtService.getStringFromPublicKey(keys.getPublic()),
        tokens.getRefreshToken()))
      throw new RuntimeException("fail to create keyStore");

    return new LoginRes(tokens, foundShop);
  }

  public User registerLocal(RegisterReq registerReq) {
    // check email
    if (shopRepo.existsByEmail(registerReq.getEmail()))
      throw new BabRequestError("shop is registered");

    return shopRepo.save(User.builder()
        .name(registerReq.getName())
        .email(registerReq.getEmail())
        .password(passwordEncoder.encode(registerReq.getPassword()))
        .build());

  }
}
