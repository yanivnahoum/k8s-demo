package com.att.training.k8s.greeting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ReverseClient {

    private final RestTemplate restTemplate;

    public ReverseClient(RestTemplateBuilder builder, @Value("${reverse.base-url}") String serviceUrl) {
        restTemplate = builder
                .rootUri(serviceUrl)
                .build();
    }

    public String reverse(String word) {
        return restTemplate.getForObject("/reverse/{word}", String.class, word);
    }
}
