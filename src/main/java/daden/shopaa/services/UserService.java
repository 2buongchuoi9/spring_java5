package daden.shopaa.services;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import daden.shopaa.Auth.JwtService;
import daden.shopaa.entity.KeyToken;
import daden.shopaa.entity.User;
import daden.shopaa.exceptions.BabRequestError;
import daden.shopaa.exceptions.NotFoundError;
import daden.shopaa.exceptions.UnauthorizeError;
import daden.shopaa.repository.KeyTokenRepo;
import daden.shopaa.repository.UserRepo;
import daden.shopaa.utils.Constans.HEADER;
import io.swagger.v3.oas.models.headers.Header;
import jakarta.servlet.http.HttpServletRequest;
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
  private UserRepo userRepo;
  @Autowired
  private KeyTokenRepo keyRepo;
  @Autowired
  private PasswordEncoder passwordEncoder;

  public LoginRes loginLocal(LoginReq loginReq) {

    // check email password
    User foundShop = userRepo.findByEmail(loginReq.getEmail())
        .orElseThrow(() -> new NotFoundError("shop is not registered"));

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
    if (userRepo.existsByEmail(registerReq.getEmail()))
      throw new BabRequestError("shop is registered");

    return userRepo.save(User.builder()
        .name(registerReq.getName())
        .email(registerReq.getEmail())
        .password(passwordEncoder.encode(registerReq.getPassword()))
        .build());
  }

  public LoginRes handleRefeshToken(HttpServletRequest req) {
    // check x-client-id in header
    String clientId = req.getHeader(HEADER.X_CLIENT_ID);
    if (clientId == null)
      throw new UnauthorizeError("x-client-id has must in header");

    KeyToken keyStore = keyRepo.findByUserId(clientId)
        .orElseThrow(() -> new UnauthorizeError("invalid x-rtoken-id in header"));

    String refreshToken = req.getHeader(HEADER.REFRESHTOKEN);
    if (refreshToken == null)
      throw new UnauthorizeError("x-rtoken-id has must in header");

    // nếu RT có trong danh sách đã dùng thì cảnh báo về bảo mật và xóa collection
    if (keyStore.getRefreshTokensUsed() != null
        && keyStore.getRefreshTokensUsed().stream().anyMatch(v -> v.equals(refreshToken))) {
      keyRepo.deleteById(keyStore.getId());
      throw new UnauthorizeError("Something wrong happend (refresh token is old) !!! Pls relogin");
    }

    String userEmail = jwtService.verifyToken(refreshToken, jwtService.getPublicKeyFromString(keyStore.getPublicKey()));
    if (userEmail == null)
      throw new UnauthorizeError("decode token is fail");

    System.out.println("::::::::::::::" + keyStore.getUserId());

    if (!keyStore.getRefreshToken().equals(refreshToken))
      throw new UnauthorizeError("User not register!!!");

    User foundUser = userRepo.findById(keyStore.getUserId())
        .orElseThrow(() -> new UnauthorizeError("invalid refresh token"));

    KeyPair keys = JwtService.generatorKeyPair();

    TokenStore newTokens = new TokenStore(jwtService.createAccessToken(foundUser.getEmail(), keys.getPrivate()),
        jwtService.createRefreshToken(foundUser.getEmail(), keys.getPrivate()));

    // add refresh vao refreshTokenUsed
    List<String> newRefreshTokenUsed = (keyStore.getRefreshTokensUsed() != null)
        ? keyStore.getRefreshTokensUsed()
        : new ArrayList<>();
    newRefreshTokenUsed.add(refreshToken);

    if (!keyTokenService.createKeyStore(
        foundUser.getId(),
        jwtService.getStringFromPublicKey(keys.getPublic()),
        newTokens.getRefreshToken(),
        newRefreshTokenUsed))
      throw new RuntimeException("fail to create keyStore");

    return new LoginRes(newTokens, foundUser);

  }
}
