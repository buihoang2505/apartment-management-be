package com.apartment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.apartment.domain")
@ConfigurationPropertiesScan(basePackages = "com.apartment")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}