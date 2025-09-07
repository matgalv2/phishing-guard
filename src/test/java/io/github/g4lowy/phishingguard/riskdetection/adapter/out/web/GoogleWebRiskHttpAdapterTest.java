package io.github.g4lowy.phishingguard.riskdetection.adapter.out.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.g4lowy.phishingguard.riskdetection.adapter.out.web.dto.ConfidenceLevel;
import io.github.g4lowy.phishingguard.riskdetection.adapter.out.web.dto.EvaluateUriResponseBody;
import io.github.g4lowy.phishingguard.riskdetection.adapter.out.web.dto.Score;
import io.github.g4lowy.phishingguard.riskdetection.adapter.out.web.dto.ThreatType;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Risk;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class GoogleWebRiskHttpAdapterTest {

    private static MockWebServer mockWebServer;
    private GoogleWebRiskHttpAdapter adapter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void setUp() {
        String baseUrl = mockWebServer.url("/").toString();
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();

        adapter = new GoogleWebRiskHttpAdapter(
                webClient,
                "/evaluateUri",
                "fake-token",
                500
        );
    }

    @Test
    void shouldReturnMalicious_whenConfidenceAboveLow() throws Exception {
        EvaluateUriResponseBody responseBody = new EvaluateUriResponseBody(
                List.of(new Score(ThreatType.SOCIAL_ENGINEERING, ConfidenceLevel.HIGH))
        );

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(responseBody))
                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));

        StepVerifier.create(adapter.evaluate("http://bad.example"))
                .expectNext(Risk.MALICIOUS)
                .verifyComplete();

        var recorded = mockWebServer.takeRequest();

        assertThat(recorded.getPath()).isEqualTo("/evaluateUri");
        assertThat(recorded.getHeader("Authorization")).isEqualTo("Bearer fake-token");
        assertThat(recorded.getHeader("Content-Type")).contains("application/json");
    }

    @Test
    void shouldReturnSafe_whenAllConfidenceLowOrEqual() throws Exception {
        EvaluateUriResponseBody responseBody = new EvaluateUriResponseBody(
                List.of(new Score(ThreatType.MALWARE, ConfidenceLevel.LOW))
        );

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(responseBody))
                .addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(200));

        StepVerifier.create(adapter.evaluate("http://good.example"))
                .expectNext(Risk.SAFE)
                .verifyComplete();
    }

    @Test
    void shouldFallbackToSuspicious_whenErrorResponse() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        StepVerifier.create(adapter.evaluate("http://unstable.example"))
                .expectNext(Risk.SUSPICIOUS)
                .verifyComplete();
    }

    @Test
    void shouldTimeoutAndReturnSuspicious_whenResponseTooSlow() {
        mockWebServer.enqueue(new MockResponse()
                .setBody("{}")
                .setBodyDelay(2, TimeUnit.SECONDS));

        StepVerifier.create(adapter.evaluate("http://slow.example"))
                .expectNext(Risk.SUSPICIOUS)
                .verifyComplete();
    }
}
