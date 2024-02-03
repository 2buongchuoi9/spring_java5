package daden.shopaa.controller;

import org.springframework.web.bind.annotation.RestController;

import daden.shopaa.dto.MainResponse;
import daden.shopaa.dto.PageCustom;
import daden.shopaa.entity.User;
import daden.shopaa.security.UserRoot;
import daden.shopaa.services.UserService;
import daden.shopaa.utils.Constans;
import daden.shopaa.utils.Constans.HASROLE;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.nio.file.attribute.UserPrincipal;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User")
public class UserController {
  private final UserService userService;

  @PostMapping("/profile")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<MainResponse<User>> getInfo(@AuthenticationPrincipal UserRoot userRoot) {
    return ResponseEntity.ok().body(MainResponse.oke("get current profile user success", userRoot.getUser()));
  }

  @Operation(summary = "casiuohdiuasn")
  @GetMapping("")
  // @PreAuthorize(HASROLE.USER + " or " + HASROLE.ADMIN + " or " + HASROLE.MOD)
  public ResponseEntity<PageCustom<User>> getAll(
      @PageableDefault(page = 0, size = 10, direction = Direction.ASC, sort = "id") Pageable pageable) {

    return ResponseEntity.ok().body(userService.findAll(pageable));
  }

}
