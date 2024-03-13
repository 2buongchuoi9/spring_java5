package daden.shopaa.controller;

import org.springframework.web.bind.annotation.RestController;

import daden.shopaa.services.MailService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
  final MailService mailService;

  @GetMapping("/send-mail")
  public String getMethodName() {
    mailService.sendMail("nguyenssk043@gmail.com", "test opt", "123456");
    return "oke";
  }

  @GetMapping("/register-owner")
  public ResponseEntity<?> getMethodName(@RequestBody RegisterOwner registerOwner) {
    return ResponseEntity.ok().body("");
  }

  /**
   * InnerTestController
   */
  @Data
  public class RegisterOwner {
    String name;
    String email;
    String phone;
    String sportName;
    String address;
    int status;
    int gender;
    String birthday;

  }

}
