package daden.shopaa.Auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import daden.shopaa.exceptions.UnauthorizeError;
import daden.shopaa.models.KeyTokenModel;
import daden.shopaa.repository.KeyTokenRepo;
import daden.shopaa.utils.Constans;
import daden.shopaa.utils.Constans.HEADER;
import daden.shopaa.utils.Constans.FREE_REQUEST;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtService jwtService;
  @Autowired
  private KeyTokenRepo keyRepo;
  @Autowired
  private UserDetailsService userDetailsService;

  final private String[] list = {
      Constans.API_V1 + "/cc",
      Constans.API_V1 + "/favicon.ico",
      FREE_REQUEST.AUTH + "/login",
      FREE_REQUEST.AUTH + "/register"
      // RequestMapping.PRODUCTS,
  };

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest req,
      @NonNull HttpServletResponse res,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    System.out.println(req.getServletPath());

    if (checkRequest(req)) {
      System.out.println("qua");
      filterChain.doFilter(req, res);
      return;
    }

    // check x-client-id in header
    String clientId = req.getHeader(HEADER.X_CLIENT_ID);
    if (clientId == null)
      throw new UnauthorizeError("x-client-id has much in header");

    KeyTokenModel keyStore = keyRepo.findByUserId(clientId)
        .orElseThrow(() -> new UnauthorizeError("invalid x-client-id in header"));

    // check authorization in header
    String token = req.getHeader(HEADER.AUTHORIZATION);
    if (token == null)
      throw new UnauthorizeError("authorization has much in header");

    // // validad token
    // if (!jwtService.validateToken(token, keyStore.getPublicKey()))
    // throw new UnauthorizeError("invalid token");

    String userEmail = jwtService.verifyToken(token, jwtService.getPublicKeyFromString(keyStore.getPublicKey()));
    if (userEmail == null)
      throw new UnauthorizeError("decode token is fail");

    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        userDetails,
        null,
        userDetails.getAuthorities());

    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(req, res);
  }

  private boolean checkRequest(HttpServletRequest req) {
    String path = req.getServletPath();
    for (String cc : list) {
      if (cc.contains(path))
        return true;
    }
    return false;
  }

}
