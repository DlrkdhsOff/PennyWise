package com.zero.pennywise.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableJpaRepositories(basePackages = "com.zero.pennywise.domain.repository")
@EnableRedisRepositories(basePackages = "com.zero.pennywise.domain.repository")
@EntityScan(basePackages = "com.zero.pennywise.domain")
@ComponentScan(basePackages = "com.zero.pennywise")
public class ApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class, args);
  }

}
