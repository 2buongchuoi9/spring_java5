package daden.shopaa.logs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import daden.shopaa.services.DiscordService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@Component
public class DiscordLogFilter implements Filter {

  @Autowired
  private DiscordService discordService; // Đảm bảo rằng bạn đã tạo DiscordService để gửi tin nhắn vào Discord

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;

    // Ghi log vào Discord với thông tin của request
    String requestUrl = httpRequest.getRequestURI();
    String message = "Request received: " + requestUrl; // Cấu trúc thông điệp bạn muốn gửi vào Discord
    discordService.sendMessage(message);

    chain.doFilter(request, response);
  }

  // Các phương thức khác của Filter
}
