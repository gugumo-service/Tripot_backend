package com.junior;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ModuleApiApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "application-domain");
        SpringApplication.run(ModuleApiApplication.class, args);
    }
}
