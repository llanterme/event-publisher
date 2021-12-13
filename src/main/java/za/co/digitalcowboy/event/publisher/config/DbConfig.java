package za.co.digitalcowboy.event.publisher.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "za.co.couponzilla.rest.repository")
public class DbConfig {
}