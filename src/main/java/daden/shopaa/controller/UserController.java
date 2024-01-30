package daden.shopaa.controller;

import org.springframework.web.bind.annotation.RestController;

import daden.shopaa.security.UserRoot;
import daden.shopaa.utils.Constans;
import daden.shopaa.utils.Constans.HASROLE;
import lombok.RequiredArgsConstructor;

import java.nio.file.attribute.UserPrincipal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

  @PostMapping("/profile")
  // @PreAuthorize(HASROLE.USER + " or " + HASROLE.ADMIN + " or " + HASROLE.MOD)
  public ResponseEntity<?> getInfo(@AuthenticationPrincipal UserRoot userRoot) {
    return ResponseEntity.ok().body(userRoot.getUser());
  }

}
