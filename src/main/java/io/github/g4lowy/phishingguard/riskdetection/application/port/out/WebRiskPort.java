package io.github.g4lowy.phishingguard.riskdetection.application.port.out;

import io.github.g4lowy.phishingguard.riskdetection.domain.model.Risk;
import reactor.core.publisher.Mono;

public interface WebRiskPort {
    Mono<Risk> evaluate(String normalizedUrl);
}
