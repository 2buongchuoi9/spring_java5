package daden.shopaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import daden.shopaa.entity.listent.ProductVariationListener;
import daden.shopaa.entity.listent.RatingListener;

@Configuration
@EnableMongoAuditing
public class MongoDbConfig {

}
