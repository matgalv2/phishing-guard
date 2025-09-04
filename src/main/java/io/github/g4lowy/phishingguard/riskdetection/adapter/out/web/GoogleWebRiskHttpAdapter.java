package io.github.g4lowy.phishingguard.riskdetection.adapter.out.web;


import io.github.g4lowy.phishingguard.riskdetection.adapter.out.web.dto.ConfidenceLevel;
import io.github.g4lowy.phishingguard.riskdetection.adapter.out.web.dto.EvaluateUriRequestBody;
import io.github.g4lowy.phishingguard.riskdetection.adapter.out.web.dto.EvaluateUriResponseBody;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Risk;
import io.github.g4lowy.phishingguard.riskdetection.application.port.out.WebRiskPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class GoogleWebRiskHttpAdapter implements WebRiskPort {

    private static final String WEB_RISK = "webRisk";

    private final WebClient webClient;
    private final String evaluateUriEndpoint;
    private final String token;
    private final long timeoutMs;

    private GoogleWebRiskHttpAdapter(@Qualifier("googleWebRiskWebClient") WebClient webClient,
                                     @Value("${app.google.webRisk.endpoint.evaluateUri}") String evaluateUriEndpoint,
                                     @Value("${app.google.webRisk.api.token}") String token,
                                     @Value("${app.google.webRisk.timeoutMs}") long timeoutMs) {

        this.webClient = webClient;
        this.evaluateUriEndpoint = evaluateUriEndpoint;
        this.token = token;
        this.timeoutMs = timeoutMs;
    }


    @Override
    @CircuitBreaker(name = WEB_RISK)
    @Retry(name = WEB_RISK)
    public Mono<Risk> evaluate(String normalizedUrl) {
        return webClient.post()
                .uri(evaluateUriEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(new EvaluateUriRequestBody(normalizedUrl))
                .retrieve()
                .bodyToMono(EvaluateUriResponseBody.class)
                .map(evaluateUriResponse ->
                        evaluateUriResponse.scores()
                                .stream()
                                .anyMatch(score -> ConfidenceLevel.isMoreDangerousThan(score.confidenceLevel(), ConfidenceLevel.LOW))
                        )
                .map(predicate -> predicate ? Risk.MALICIOUS : Risk.SAFE)
                .timeout(Duration.ofMillis(timeoutMs));
    }
}