package daden.shopaa.services;

import org.springframework.stereotype.Service;

import daden.shopaa.entity.OtpToken;
import daden.shopaa.entity.User;
import daden.shopaa.repository.OtpRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class OtpService {
  final OtpRepo otpRepo;

  public OtpToken createOtp(User user) {
    return otpRepo.save(OtpToken.builder().userId(user.getId()).token(randomOtp()).build());
  }

  private Integer randomOtp() {
    return (int) (Math.random() * (999999 - 100000));
  }
}
