package daden.shopaa.services;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import daden.shopaa.entity.KeyToken;
import daden.shopaa.entity.User;
import daden.shopaa.exceptions.BabRequestError;
import daden.shopaa.exceptions.NotFoundError;
import daden.shopaa.exceptions.UnauthorizeError;
import daden.shopaa.repository.KeyTokenRepo;
import daden.shopaa.repository.UserRepo;
import daden.shopaa.repository.repositoryUtils.PageCustom;
import daden.shopaa.security.UserRoot;
import daden.shopaa.security.jwt.JwtService;
import daden.shopaa.utils.Constans.HEADER;
import daden.shopaa.utils._enum.AuthTypeEnum;
import daden.shopaa.utils._enum.RoleShopEnum;
import io.swagger.v3.oas.models.headers.Header;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import daden.shopaa.dto.parampetterRequest.UserParamRequest;
import daden.shopaa.dto.req.ChangePasswordReq;
import daden.shopaa.dto.req.LoginReq;
import daden.shopaa.dto.req.RegisterReq;
import daden.shopaa.dto.res.LoginRes;
import daden.shopaa.dto.res.LoginRes.TokenStore;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class UserService {
  private final MongoTemplate mongoTemplate;
  private final JwtService jwtService;
  private final KeyTokenService keyTokenService;
  private final UserRepo userRepo;
  private final KeyTokenRepo keyRepo;
  private final PasswordEncoder passwordEncoder;
  private final String AFTER_EMAIL = "@user.mod";

  public LoginRes loginLocal(LoginReq loginReq) {
    // check email password
    User foundShop = userRepo.findByEmail(loginReq.getEmail())
        .orElseThrow(() -> new NotFoundError("shop is not registered"));

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

  public User createUserMod(String ipAddress) {
    User foundUser = userRepo.findByEmail(ipAddress + AFTER_EMAIL).orElse(
        User.builder()
            .email(ipAddress + AFTER_EMAIL)
            .name(ipAddress)
            .password(ipAddress)
            .roles(Set.of(RoleShopEnum.MOD))
            .build());
    return userRepo.save(foundUser);
  }

  public User converModToUser(String userModId, RegisterReq registerReq) {
    if (userRepo.existsByEmail(registerReq.getEmail()))
      throw new BabRequestError("shop is registered");

    User foundUser = userRepo.findByIdAndRolesIn(userModId, Set.of(RoleShopEnum.MOD))
        .orElseThrow(() -> new NotFoundError("userModId", userModId));

    // if (!foundUser.getEmail().equals(ipAddress + AFTER_EMAIL))
    // throw new BabRequestError("pls used computer or phone with conver user");

    foundUser.setEmail(registerReq.getEmail());
    foundUser.setName(registerReq.getName());
    foundUser.setPassword(passwordEncoder.encode(registerReq.getPassword()));
    foundUser.setRoles(Set.of(RoleShopEnum.USER));

    return userRepo.save(foundUser);

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

  public PageCustom<User> findAll(Pageable pageable, UserParamRequest paramRequest) {
    String keySearch = paramRequest.getKeySearch();
    Boolean status = paramRequest.getStatus();
    Boolean verify = paramRequest.getVerify();
    AuthTypeEnum authType = paramRequest.getAuthType();

    Query query = new Query();

    if (keySearch != null && !keySearch.isEmpty()) {
      String regexPattern = "(?i)" + keySearch.trim(); // Thêm ?i để không phân biệt chữ hoa chữ thường
      query.addCriteria(new Criteria().orOperator(
          Criteria.where("name").regex(regexPattern),
          Criteria.where("email").regex(regexPattern)));
    }

    if (status != null)
      query.addCriteria(Criteria.where("status").is(status));

    if (verify != null)
      query.addCriteria(Criteria.where("verify").is(verify));

    if (authType != null)
      query.addCriteria(Criteria.where("authType").is(authType));

    query.with(pageable);

    List<User> list = mongoTemplate.find(query, User.class);
    long total = mongoTemplate.count(query, User.class);

    return new PageCustom<User>(PageableExecutionUtils.getPage(list, pageable, () -> total));
  }

  public boolean handleChangePassword(ChangePasswordReq passwordReq) {
    // check email
    User foundUser = userRepo.findByEmail(passwordReq.getEmail())
        .orElseThrow(() -> new NotFoundError("email", passwordReq.getEmail()));

    if (!passwordEncoder.matches(passwordReq.getPassword(), foundUser.getPassword()))
      throw new BabRequestError("password is not true");

    foundUser.setPassword(passwordEncoder.encode(passwordReq.getPasswordNew()));
    userRepo.save(foundUser);

    return true;
  }

}
