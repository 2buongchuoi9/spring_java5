package daden.shopaa;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import daden.shopaa.models.ShopModel;

import daden.shopaa.repository.ShopRepo;
import daden.shopaa.req.LoginReq;
import daden.shopaa.res.LoginRes;
import daden.shopaa.services.ShopService;
import daden.shopaa.utils._enum.RoleShopEnum;

@SpringBootApplication
public class SpringJava5Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringJava5Application.class, args);
	}

	// @Bean
	// CommandLineRunner runner(ShopService shopService) {
	// return args -> {
	// LoginRes l =
	// shopService.loginLocal(LoginReq.builder().email("root@gmail.com").password("123").build());
	// System.out.println(l.getAccessToken());
	// System.out.println(l.getRefreshToken());
	// System.out.println(l.getShop());
	// };
	// }

}
