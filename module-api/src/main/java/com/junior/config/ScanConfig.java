package com.junior.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"com.junior.domain"})
@EnableJpaRepositories(basePackages = {"com.junior.domain"})
@PropertySource(ignoreResourceNotFound = true,
        value = {
                "classpath:application-domain-${spring.profiles.active}.yml",
                "classpath:application-common-${spring.profiles.active}.yml",
                "classpath:application-api-${spring.profiles.active}.yml"
        }, factory = YamlPropertySourceFactory.class
)
public class ScanConfig {
}
