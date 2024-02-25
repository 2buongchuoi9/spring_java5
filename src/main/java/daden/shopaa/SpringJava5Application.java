package daden.shopaa;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import daden.shopaa.entity.User;
import daden.shopaa.repository.UserRepo;

import daden.shopaa.services.UserService;
import daden.shopaa.utils.CookieUtils;
import daden.shopaa.utils._enum.RoleShopEnum;

@SpringBootApplication
public class SpringJava5Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringJava5Application.class, args);
	}

	// @Bean
	// CommandLineRunner runner() {
	// return args -> {
	// };
	// }

}
