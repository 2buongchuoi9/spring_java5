package daden.shopaa.applicationEvenConfig.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import daden.shopaa.applicationEvenConfig.event.OnRegisterCompleteEvent;
import daden.shopaa.entity.OtpToken;
import daden.shopaa.entity.User;
import daden.shopaa.services.MailService;
import daden.shopaa.services.OtpService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RegisterListener implements ApplicationListener<OnRegisterCompleteEvent> {

  final private OtpService otpService;
  final private MailService mailService;

  @Override
  public void onApplicationEvent(OnRegisterCompleteEvent event) {
    User user = event.getUser();

    OtpToken otpToken = otpService.createOtp(user);

    mailService.sendMail(user.getEmail(), "Otp from java5",
        "ma otp: " + otpToken.getToken() + "\nCo hieu luc trong 3 phut");

  }

}
