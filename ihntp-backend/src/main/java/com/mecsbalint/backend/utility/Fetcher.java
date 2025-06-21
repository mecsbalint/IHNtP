package com.mecsbalint.backend.utility;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class Fetcher {
    private final WebClient webClient;

    public Fetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> T getFetch(String url, Class<T> returnClass) {
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(returnClass)
                .block();
    }

    public <ResponseBodyType, RequestBodyType> ResponseBodyType postFetch(String url, Class<ResponseBodyType> returnClass, RequestBodyType requestBody) {
        return webClient
                .post()
                .uri(url)
                .header("Content-Type", "application/json")
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(returnClass)
                .block();
    }
}
