package com.mecsbalint.backend.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public WebClient getWebClient() {
        return WebClient.create();
    }

    @Bean
    public UUID getUUID() {
        return UUID.randomUUID();
    }

    @Bean
    public Logger getLogger() {
        return LoggerFactory.getLogger("IHNtP Backend");
    }
}
