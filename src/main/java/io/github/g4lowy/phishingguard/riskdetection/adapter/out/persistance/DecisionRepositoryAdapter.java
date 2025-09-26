package io.github.g4lowy.phishingguard.riskdetection.adapter.out.persistance;

import io.github.g4lowy.phishingguard.riskdetection.adapter.out.persistance.model.DecisionEntity;
import io.github.g4lowy.phishingguard.riskdetection.application.port.out.DecisionRepositoryPort;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Decision;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Sms;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
class DecisionRepositoryAdapter implements DecisionRepositoryPort {

    private final R2dbcDecisionRepository decisionRepository;

    @Override
    public Mono<Void> saveDecision(Sms sms, Decision decision) {

        DecisionEntity decisionEntity = DecisionEntity.builder()
                .sender(sms.sender())
                .recipient(sms.recipient())
                .disposition(decision.disposition().name())
                .reason(decision.reason().name())
                .urls(getDecisionUrlsAsArray(decision)).build();

        return decisionRepository.save(decisionEntity).then();

    }

    private String[] getDecisionUrlsAsArray(Decision decision) {

        return decision.checkedUrls().toArray(String[]::new);
    }
}
