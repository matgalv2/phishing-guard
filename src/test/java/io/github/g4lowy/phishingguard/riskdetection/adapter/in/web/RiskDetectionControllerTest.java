package io.github.g4lowy.phishingguard.riskdetection.adapter.in.web;

import io.github.g4lowy.phishingguard.common.dto.SmsDto;
import io.github.g4lowy.phishingguard.riskdetection.adapter.out.web.WebRiskClientConnectionException;
import io.github.g4lowy.phishingguard.riskdetection.application.dto.DecisionResponseDto;
import io.github.g4lowy.phishingguard.riskdetection.application.port.in.EvaluateSmsRiskUseCase;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Disposition;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Reason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@Import(RiskDetectionControllerExceptionHandler.class)
@WebFluxTest(controllers = RiskDetectionController.class)
class RiskDetectionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private EvaluateSmsRiskUseCase evaluateSmsRiskUseCase;

    @Test
    @DisplayName("POST /sms -> 200 and returns DecisionResponseDto")
    void handleSmsWithoutUrl_success() {
        DecisionResponseDto responseBody = new DecisionResponseDto(Disposition.ALLOWED.name(), Reason.NO_URLS.name());

        when(evaluateSmsRiskUseCase.evaluate(any(SmsDto.class)))
                .thenReturn(Mono.just(responseBody));

        String validJson = """
            {
              "sender":"123456789",
              "recipient":"987654321",
              "message":"Hello World"
            }
            """;

        webTestClient.post()
                .uri("/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validJson)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.disposition").isEqualTo(Disposition.ALLOWED.name())
                .jsonPath("$.reason").isEqualTo(Reason.NO_URLS.name());
    }


    @Test
    @DisplayName("POST /sms with invalid phone numbers -> 400")
    void handleSms_invalidPhoneNumber() {
        String invalidJson = """
            {
              "sender":"12",        // too short
              "recipient":"abcdefghi", // not digits
              "message":"Hi"
            }
            """;

        webTestClient.post()
                .uri("/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidJson)
                .exchange()
                .expectStatus().isBadRequest();

        verifyNoInteractions(evaluateSmsRiskUseCase);
    }

    @Test
    @DisplayName("POST /sms with blank message -> 400")
    void handleSms_blankMessage() {
        String invalidJson = """
            {
              "sender":"123456789",
              "recipient":"987654321",
              "message":"   "
            }
            """;

        webTestClient.post()
                .uri("/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidJson)
                .exchange()
                .expectStatus().isBadRequest();

        verifyNoInteractions(evaluateSmsRiskUseCase);
    }

    @Test
    @DisplayName("POST /sms -> 500 when WebRiskClientConnectionException is thrown")
    void handleSms_webRiskClientConnectionException() {
        when(evaluateSmsRiskUseCase.evaluate(any(SmsDto.class)))
                .thenReturn(Mono.error(new WebRiskClientConnectionException("Connection failed")));

        String validJson = """
            {
              "sender":"123456789",
              "recipient":"987654321",
              "message":"Suspicious link http://phishy.com"
            }
            """;

        webTestClient.post()
                .uri("/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validJson)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.disposition").isEqualTo("BLOCKED")
                .jsonPath("$.reason").isEqualTo("SUSPICIOUS_URL");
    }
}
