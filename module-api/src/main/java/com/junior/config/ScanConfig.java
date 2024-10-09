package com.junior.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.junior.domain")
@EnableJpaRepositories("com.junior.repository")
public class ScanConfig {
}
