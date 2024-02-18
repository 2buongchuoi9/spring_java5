// package daden.shopaa.config;

// import java.text.DateFormat;
// import java.text.SimpleDateFormat;
// import java.time.format.DateTimeFormatter;

// import
// org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.SerializationFeature;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
// import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

// @Configuration
// @SuppressWarnings("null")
// public class WebMvcConfig {

// private static final String dateFormat = "dd-MM-yyyy";
// private static final String dateTimeFormat = "dd-MM-yyyy HH:mm:ss";

// // @Bean
// // public Jackson2ObjectMapperBuilderCustomizer customizeObjectMapper() {
// // return builder -> {
// // // Đăng ký module JavaTimeModule để hỗ trợ đối tượng thời gian Java 8
// // builder.modules(new JavaTimeModule());
// // // Tắt việc viết ngày tháng dưới dạng timestamp
// // builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
// // // Định dạng ngày tháng tùy chỉnh
// // builder.simpleDateFormat(dateTimeFormat);
// // // Thiết lập định dạng ngày tháng cho ObjectMapper
// // builder.dateFormat(new SimpleDateFormat(dateTimeFormat));
// // };
// // }

// @Bean
// public WebMvcConfigurer corsConfig() {
// return new WebMvcConfigurer() {
// @Override
// public void addCorsMappings(CorsRegistry registry) {
// registry.addMapping("/**")
// .allowedHeaders("*")
// .allowedMethods("GET", "POST", "OPTIONS")
// .allowedOrigins("http://localhost:5173")
// .allowCredentials(true);
// }
// };
// }
// }
