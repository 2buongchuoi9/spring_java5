package daden.shopaa.services;

import java.security.KeyPair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import daden.shopaa.Auth.JwtService;
import daden.shopaa.exceptions.BabRequestError;
import daden.shopaa.exceptions.NotFoundException;
import daden.shopaa.exceptions.UnKnowError;
import daden.shopaa.models.ShopModel;
import daden.shopaa.repository.ShopRepo;
import daden.shopaa.req.LoginReq;
import daden.shopaa.req.RegisterReq;
import daden.shopaa.res.LoginRes;
import daden.shopaa.res.LoginRes.TokenStore;

@Service
public class ShopService {
  @Autowired
  private JwtService jwtService;
  @Autowired
  private KeyTokenService keyTokenService;
  @Autowired
  private ShopRepo shopRepo;
  @Autowired
  private PasswordEncoder passwordEncoder;

  public LoginRes loginLocal(LoginReq loginReq) {

    // check email password
    ShopModel foundShop = shopRepo.findByEmail(loginReq.getEmail())
        .orElseThrow(() -> new NotFoundException("shop is not registered"));

    KeyPair keys = JwtService.generatorKeyPair();

    TokenStore tokens = new TokenStore(jwtService.createAccessToken(loginReq.getEmail(), keys.getPrivate()),
        jwtService.createRefreshToken(loginReq.getEmail(), keys.getPrivate()));

    if (!keyTokenService.createKeyStore(
        foundShop.getId(),
        jwtService.getStringFromPublicKey(keys.getPublic()),
        tokens.getRefreshToken()))
      throw new UnKnowError("fail to create keyStore");

    return new LoginRes(tokens, foundShop);
  }

  public ShopModel registerLocal(RegisterReq registerReq) {

    // check email password
    if (shopRepo.existsByEmail(registerReq.getEmail()))
      throw new BabRequestError("shop is registered");

    return shopRepo.save(ShopModel.builder()
        .name(registerReq.getName())
        .email(registerReq.getEmail())
        .password(passwordEncoder.encode(registerReq.getPassword()))
        .build());

  }
}
