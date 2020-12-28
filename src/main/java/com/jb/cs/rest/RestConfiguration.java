package com.jb.cs.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RestConfiguration {

    private static Logger LOGGER = LoggerFactory.getLogger(RestConfiguration.class);

    @Bean(name = "tokens")
    public Map<String, ClientSession> tokensMap() {
        return new HashMap<>();
    }

    /*@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins("*")
                        .allowedHeaders("*");
            }
        };
    }*/

    @Bean
    @ConditionalOnExpression("${rest.cors}")
    public WebMvcConfigurer corsConfigurer()
    {
        LOGGER.warn("Global cross origin is enabled");
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry)
            {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .allowedOrigins("http://localhost:4200")
                        .allowCredentials(true)
                        .allowedHeaders("*");
            }
        };
    }

}

