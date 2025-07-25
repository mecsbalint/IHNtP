package com.mecsbalint.backend.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Configuration
public class AppConfig {

    @Bean
    public WebClient getWebClient(WebClient.Builder builder) {
        return builder.build();
    }

    @Bean
    public Logger getLogger() {
        return LoggerFactory.getLogger("IHNtP Backend");
    }
}
