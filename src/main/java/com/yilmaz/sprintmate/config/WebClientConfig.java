package com.yilmaz.sprintmate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * WebClient Configuration
 * 
 * HTTP client for external API calls (GitHub, OpenAI, etc.)
 */
@Slf4j
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }

    /**
     * Request logging filter
     */
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (log.isDebugEnabled()) {
                log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
            }
            return Mono.just(clientRequest);
        });
    }

    /**
     * Response logging filter
     */
    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (log.isDebugEnabled()) {
                log.debug("Response status: {}", clientResponse.statusCode());
            }
            return Mono.just(clientResponse);
        });
    }
}
