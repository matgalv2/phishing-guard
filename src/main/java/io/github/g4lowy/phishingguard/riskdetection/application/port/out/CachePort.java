package io.github.g4lowy.phishingguard.riskdetection.application.port.out;

import io.github.g4lowy.phishingguard.riskdetection.domain.model.Risk;
import reactor.core.publisher.Mono;

public interface CachePort {

    Mono<Risk> getUrl(String urlHash);

    Mono<Void> putUrl(String urlHash, Risk risk, long ttlSeconds);

    Mono<Risk> getETldPlusOne(String e2ld);

    Mono<Void> putETldPlusOne(String e2ld, Risk risk, long ttlSeconds);
}
