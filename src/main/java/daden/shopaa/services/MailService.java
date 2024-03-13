package daden.shopaa.services;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
  final private JavaMailSender mailSender;

  public void sendMail(String email, String subject, String text) {
    try {
      SimpleMailMessage sEmail = new SimpleMailMessage();
      sEmail.setTo(email);
      sEmail.setSubject(subject);
      sEmail.setText(text);
      mailSender.send(sEmail);

    } catch (MailException e) {
      e.printStackTrace();
    }
  }

}