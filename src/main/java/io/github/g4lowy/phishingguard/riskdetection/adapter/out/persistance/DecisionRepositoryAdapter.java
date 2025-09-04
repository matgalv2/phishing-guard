package io.github.g4lowy.phishingguard.riskdetection.adapter.out.persistance;

import io.github.g4lowy.phishingguard.riskdetection.application.port.out.DecisionRepositoryPort;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Decision;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Sms;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DecisionRepositoryAdapter implements DecisionRepositoryPort {

    private final DatabaseClient db;

    @Override
    public Mono<Void> saveDecision(Sms sms, Decision decision) {
        return db.sql("""
            INSERT INTO decisions(sender, recipient, disposition, reason, urls)
            VALUES (:sender,:recipient,:disposition,:reason,:urls)
        """)
                .bind("sender", sms.sender())
                .bind("recipient", sms.recipient())
                .bind("disposition", decision.disposition().name())
                .bind("reason", decision.reason().name())
                .bind("urls", getDecisionUrlsAsArray(decision))
                .then();
    }

    private String[] getDecisionUrlsAsArray(Decision decision) {

        return decision.checkedUrls().toArray(String[]::new);
    }
}