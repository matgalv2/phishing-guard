package io.github.g4lowy.phishingguard.riskdetection.application.port.out;

import io.github.g4lowy.phishingguard.riskdetection.domain.model.Decision;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Sms;
import reactor.core.publisher.Mono;

public interface DecisionRepositoryPort {

    Mono<Void> saveDecision(Sms sms, Decision decision);
}
