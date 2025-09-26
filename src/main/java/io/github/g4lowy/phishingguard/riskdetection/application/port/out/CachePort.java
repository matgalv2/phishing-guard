package io.github.g4lowy.phishingguard.riskdetection.application.port.out;

import io.github.g4lowy.phishingguard.riskdetection.domain.model.Risk;
import reactor.core.publisher.Mono;

public interface CachePort {

    Mono<Risk> getUrl(String urlHash);

    Mono<Void> putUrl(String urlHash, Risk risk);

    Mono<Risk> getETldPlusOne(String eTldPlusOne);

    Mono<Void> putETldPlusOne(String eTldPlusOne, Risk risk);
}
