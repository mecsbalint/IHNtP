package com.mecsbalint.backend.utility;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class Fetcher {
    private final WebClient webClient;

    public Fetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    public <ResponseBodyType> ResponseBodyType fetch(String url, Class<ResponseBodyType> returnClass) {
        return fetch(url, returnClass, HttpMethod.GET, null, null);
    }

    public <ResponseBodyType, RequestBodyType> ResponseBodyType fetch(String url, Class<ResponseBodyType> returnClass, HttpMethod method, String contentType, RequestBodyType requestBody) {

        WebClient.RequestBodySpec requestBodySpec = webClient
                .method(method)
                .uri(url);

        if (requestBody != null && method != HttpMethod.GET && contentType != null) {
            return requestBodySpec
                    .header("Content-Type", "application/json")
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(returnClass)
                    .block();
        } else {
            return requestBodySpec
                    .retrieve()
                    .bodyToMono(returnClass)
                    .block();
        }
    }

    public String fetchContentType(String url) {
        return webClient
                .method(HttpMethod.HEAD)
                .uri(url)
                .retrieve()
                .toBodilessEntity()
                .mapNotNull(response -> {
                    MediaType contentType = response.getHeaders().getContentType();
                    return contentType == null ? null : contentType.toString();
                })
                .block();
    }
}
