package com.example.demo.redis.localhost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@EnableJpaRepositories("com.example.demo.redis.localhost.repositorys")
@SpringBootApplication
public class DemoRedisLocalHostApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoRedisLocalHostApplication.class, args);
	}

}
