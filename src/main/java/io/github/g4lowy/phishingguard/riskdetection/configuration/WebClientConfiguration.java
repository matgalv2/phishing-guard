package io.github.g4lowy.phishingguard.riskdetection.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;
import static org.springframework.http.HttpHeaders.ACCEPT;


@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient googleWebRiskWebClient(@Value("${app.google.webRisk.url}") String googleWebRiskUrl){
        return WebClient
                .builder()
                .baseUrl(googleWebRiskUrl)
                .defaultHeader(ACCEPT, APPLICATION_JSON)
                .build();
    }

}
