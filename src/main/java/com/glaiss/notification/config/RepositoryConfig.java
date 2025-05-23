package com.glaiss.notification.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.glaiss.notification.domain.model")
@EnableJpaRepositories(basePackages = {"com.glaiss.notification.domain.repository"})
public class RepositoryConfig {

}

