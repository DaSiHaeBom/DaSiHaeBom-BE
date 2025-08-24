package com.project.dasihaebom.global.config;

import com.project.dasihaebom.global.security.repository.CustomCookieCsrfTokenRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CsrfConfig {

    @Bean
    public CustomCookieCsrfTokenRepository customCookieCsrfTokenRepository() {
        return new CustomCookieCsrfTokenRepository();
    }
}
