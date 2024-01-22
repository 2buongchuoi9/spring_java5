package daden.shopaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import daden.shopaa.Auth.JwtAuthenticationFilter;
import daden.shopaa.exceptions.CustomExceptionHandler;
import daden.shopaa.utils.Constans;
import daden.shopaa.utils.Constans.FREE_REQUEST;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

  private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**",
      Constans.API_V1 + "/cc",
      Constans.API_V1 + "/favicon.ico",
      Constans.API_V1 + "/cc",
      Constans.API_V1 + "/favicon.ico",
      FREE_REQUEST.AUTH + "/login",
      FREE_REQUEST.AUTH + "/register",
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

  @Autowired
  private final JwtAuthenticationFilter jwtAuthFilter;
  @Autowired
  private final AuthenticationProvider authenticationProvider;

  // private final LogoutHandler logoutHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            req -> req.requestMatchers(WHITE_LIST_URL).permitAll()
                // req -> req.requestMatchers("/**").permitAll()
                .anyRequest()
                .authenticated())
        .authenticationProvider(authenticationProvider)
        .httpBasic(AbstractHttpConfigurer::disable)
        // .httpBasic(httpBasic ->
        // httpBasic.authenticationEntryPoint(customBasciAuthEntryPoint))
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        // .exceptionHandling(handling ->
        // handling.authenticationEntryPoint(customBasciAuthEntryPoint))
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS));
    // .logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
    // .addLogoutHandler(logoutHandler)
    // .logoutSuccessHandler((request, response, authentication) ->
    // SecurityContextHolder.clearContext()));

    return http.build();
  }

}
