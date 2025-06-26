package com.mecsbalint.backend.configuration;

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
}
