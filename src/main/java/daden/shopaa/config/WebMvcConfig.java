package daden.shopaa.config;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.format.Formatter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Configuration
@SuppressWarnings("null")
public class WebMvcConfig {

  // private static final String dateFormat = "dd-MM-yyyy";
  @Value("${den.date.format}")
  public static final String dateTimeFormat = "dd-MM-yyyy HH:mm:ss";

  @Bean
  public Formatter<LocalDateTime> localDateTimeFormatter() {
    return new Formatter<LocalDateTime>() {
      @Override
      public LocalDateTime parse(String text, Locale locale) throws ParseException {
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(dateTimeFormat));
      }

      @Override
      public String print(LocalDateTime object, Locale locale) {
        return DateTimeFormatter.ofPattern(dateTimeFormat).format(object);
      }
    };
  }

  @Bean
  public WebMvcConfigurer corsConfig() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedHeaders("*")
            .allowedMethods("GET", "POST", "OPTIONS")
            .allowedOrigins("http://localhost:5173")
            .allowCredentials(true);
      }
    };
  }
}
