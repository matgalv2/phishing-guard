package io.github.g4lowy.phishingguard.riskdetection.adapter.out.persistance;


import io.github.g4lowy.phishingguard.common.IntegrationTestBase;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Decision;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Disposition;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Reason;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Sms;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.util.List;

@DataR2dbcTest
@Testcontainers
@Import(DecisionRepositoryAdapter.class)
class DecisionRepositoryAdapterTest extends IntegrationTestBase {

    @Autowired
    private DecisionRepositoryAdapter decisionRepository;

    @Autowired
    private R2dbcDecisionRepository decisionRepo;

    @BeforeEach
    void clean() {
        decisionRepo.deleteAll().block();
    }

    @Test
    void shouldSaveDecision() {
        Sms sms = new Sms("12345", "67890", "Suspicious message http://evil.org");
        Decision decision = new Decision(
                Disposition.BLOCKED,
                Reason.MALICIOUS_URL,
                List.of("http://evil.org")
        );

        StepVerifier.create(decisionRepository.saveDecision(sms, decision))
                .verifyComplete();

        StepVerifier.create(decisionRepo.findAll().single())
                .assertNext(entity -> {
                    assert entity.getSender().equals("12345");
                    assert entity.getRecipient().equals("67890");
                    assert entity.getDisposition().equals(Disposition.BLOCKED.name());
                    assert entity.getReason().equals(Reason.MALICIOUS_URL.name());
                    assert entity.getUrls().length == 1;
                })
                .verifyComplete();
    }
}
