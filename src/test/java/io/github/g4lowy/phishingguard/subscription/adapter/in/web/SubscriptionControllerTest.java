package io.github.g4lowy.phishingguard.subscription.adapter.in.web;

import io.github.g4lowy.phishingguard.common.dto.SmsDto;
import io.github.g4lowy.phishingguard.subscription.application.dto.ServiceStatusDto;
import io.github.g4lowy.phishingguard.subscription.application.port.in.SubscriptionManagementSmsUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = SubscriptionController.class)
class SubscriptionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private SubscriptionManagementSmsUseCase subscriptionManagementSmsUseCase;

    @Test
    @DisplayName("POST /subscription -> 200 and returns ServiceStatusDto")
    void handleSubscriptionRequest_success() {
        ServiceStatusDto responseBody = new ServiceStatusDto("Phishing guard service is active");

        when(subscriptionManagementSmsUseCase.handleSubscriptionSms(any(SmsDto.class)))
                .thenReturn(Mono.just(responseBody));

        String validJson = """
            {
              "sender":"123456789",
              "recipient":"987654321",
              "message":"START"
            }
            """;

        webTestClient.post()
                .uri("/subscription")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(validJson)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Phishing guard service is active");
    }

    @Test
    @DisplayName("POST /subscription with invalid phone numbers -> 400")
    void handleSubscriptionRequest_invalidPhoneNumber() {
        String invalidJson = """
            {
              "sender":"123",        // too short
              "recipient":"abcdefgh", // not digits
              "message":"SUBSCRIBE"
            }
            """;

        webTestClient.post()
                .uri("/subscription")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidJson)
                .exchange()
                .expectStatus().isBadRequest();

        verifyNoInteractions(subscriptionManagementSmsUseCase);
    }

    @Test
    @DisplayName("POST /subscription with blank message -> 400")
    void handleSubscriptionRequest_blankMessage() {
        String invalidJson = """
            {
              "sender":"123456789",
              "recipient":"987654321",
              "message":"   "
            }
            """;

        webTestClient.post()
                .uri("/subscription")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidJson)
                .exchange()
                .expectStatus().isBadRequest();

        verifyNoInteractions(subscriptionManagementSmsUseCase);
    }
}