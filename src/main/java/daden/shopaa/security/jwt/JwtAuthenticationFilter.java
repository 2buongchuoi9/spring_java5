package daden.shopaa.security.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import daden.shopaa.entity.KeyToken;
import daden.shopaa.exceptions.BabRequestError;
import daden.shopaa.exceptions.UnauthorizeError;
import daden.shopaa.repository.KeyTokenRepo;
import daden.shopaa.security.UserRootService;
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
  private UserRootService userRootService;

  @Autowired
  @Qualifier("handlerExceptionResolver")
  private HandlerExceptionResolver exceptionResolver;

  final private List<String> list = Arrays.asList(
      Constans.API_V1 + "/cc",
      Constans.API_V1 + "/favicon.ico",
      FREE_REQUEST.AUTH + "/login",
      FREE_REQUEST.AUTH + "/register",
      FREE_REQUEST.AUTH + "/refresh-token",
      Constans.API_V1 + "/oauth2",
      "/api/v1/category/**",
      "/login",
      "/test-1",
      "/v2/api-docs",
      "/v3/api-docs",
      "/v3/api-docs",
      "/swagger-resources",
      "/swagger-resources",
      "/configuration/ui",
      "/configuration/security",
      "/swagger-ui",
      "/webjars",
      "/swagger-ui.html"
  // RequestMapping.PRODUCTS,
  );

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest req,
      @NonNull HttpServletResponse res,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    try {
      System.out.println(req.getMethod() + "::::" + req.getServletPath());

      if (req.getMethod().equals("GET") || checkRequest(req)) {
        System.out.println("qua");
        filterChain.doFilter(req, res);
        return;
      }

      System.out.println("test::::::::" + req.getHeader(HEADER.AUTHORIZATION));

      // check x-client-id in header
      String clientId = req.getHeader(HEADER.X_CLIENT_ID);
      if (clientId == null)
        throw new UnauthorizeError("x-client-id has must in header");

      KeyToken keyStore = keyRepo.findByUserId(clientId)
          .orElseThrow(() -> new UnauthorizeError("invalid x-client-id in header"));

      // check authorization in header
      String token = req.getHeader(HEADER.AUTHORIZATION);
      if (token == null)
        throw new UnauthorizeError("authorization has must in header");

      String userEmail = jwtService.verifyToken(token, jwtService.getPublicKeyFromString(keyStore.getPublicKey()));
      if (userEmail == null)
        throw new UnauthorizeError("decode token is fail");

      UserDetails userDetails = userRootService.loadUserByUsername(userEmail);

      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities());

      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
      SecurityContextHolder.getContext().setAuthentication(authToken);

      filterChain.doFilter(req, res);
    } catch (Exception e) {
      System.out.println(":::::::::::::::::" + e.getMessage());
      exceptionResolver.resolveException(req, res, null, e);
    }
  }

  private boolean checkRequest(HttpServletRequest req) {
    String path = req.getServletPath();
    return list.stream().anyMatch(path::startsWith);
  }

}
