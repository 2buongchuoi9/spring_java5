package daden.shopaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import daden.shopaa.entity.User;
import daden.shopaa.exceptions.CustomExceptionHandler;
import daden.shopaa.repository.UserRepo;
import daden.shopaa.security.CustomOAuth2UserService;
import daden.shopaa.security.HttpCookieOAuth2AuthorizationRequestRepository;
import daden.shopaa.security.OAuth2LoginSuccessHandler;
import daden.shopaa.security.UserRoot;
import daden.shopaa.security.UserRootService;
import daden.shopaa.security.jwt.JwtAuthenticationFilter;
import daden.shopaa.utils.Constans;
import daden.shopaa.utils.Constans.FREE_REQUEST;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

  final JwtAuthenticationFilter jwtAuthFilter;
  final UserRootService userRootService;
  final CustomOAuth2UserService customOAuth2UserService;
  final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
  final HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository;

  private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**",
      "/api/v1/auth/conver-mod-to-user",
      "/login",
      Constans.API_V1 + "/cc",
      FREE_REQUEST.AUTH + "/login",
      FREE_REQUEST.AUTH + "/register",
      Constans.API_V1 + "/favicon.ico",
      Constans.API_V1 + "/oauth2",
      "/api/v1/cart/add-to-cart",
      "/api/v1/order/checkout-review",
      "/v2/api-docs",
      "/v3/api-docs",
      "/v3/api-docs/**",
      "/swagger-resources",
      "/swagger-resources/**",
      "/configuration/ui",
      "/configuration/security",
      "/swagger-ui/**",
      "/webjars/**",
      "/swagger-ui.html" };

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userRootService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        // .cors(c -> c.configurationSource(corsConfigurationSource()))
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(
            req -> req
                .requestMatchers(WHITE_LIST_URL).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                .anyRequest()
                .authenticated())
        .formLogin(f -> f.disable())
        .httpBasic(v -> v.disable())
        .oauth2Login(o -> o
            .authorizationEndpoint(
                v -> v
                    .baseUri("/api/v1/oauth2/authorization")
                    .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository))
            .redirectionEndpoint(v -> v.baseUri("/api/v1/oauth2/callback/*"))
            .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
            .successHandler(oAuth2LoginSuccessHandler)
        // .failureHandler(null)
        )
        .authenticationProvider(authenticationProvider())
        // .addFilterBefore(corsFilter(),
        // OAuth2AuthorizationRequestRedirectFilter.class)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS));

    return http.build();
  }

  // @Bean
  // public CorsFilter corsFilter() {
  // UrlBasedCorsConfigurationSource source = new
  // UrlBasedCorsConfigurationSource();
  // CorsConfiguration config = new CorsConfiguration();
  // config.setAllowCredentials(true);
  // // config.addAllowedOrigin("http://localhost:5173");
  // config.setAllowedOrigins(List.of("http://localhost:5173",
  // "https://accounts.google.com"));
  // config.addAllowedHeader("*");
  // config.addAllowedMethod("*");
  // config.setMaxAge(3600l);
  // source.registerCorsConfiguration("/api/v1/oauth2/authorization/google",
  // config);
  // source.registerCorsConfiguration("/**", config);
  // return new CorsFilter(source);
  // }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:5173"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
