package com.ricardo.pmapp.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final AppConfig appConfig;

    public CorsConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] origins = this.appConfig.getCors().getAllowedCorsOrigins().toArray(new String[0]);
        registry
                .addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .exposedHeaders("Auth-Status");
    }
}
