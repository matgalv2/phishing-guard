package io.github.g4lowy.phishingguard.riskdetection.application.service;


import io.github.g4lowy.phishingguard.riskdetection.domain.model.Risk;
import io.github.g4lowy.phishingguard.riskdetection.application.port.out.CachePort;
import io.github.g4lowy.phishingguard.riskdetection.application.port.out.WebRiskPort;
import io.github.g4lowy.phishingguard.riskdetection.domain.riskchecker.UrlRiskCheckerStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UrlEvaluateService {

    private final WebRiskPort webRisk;
    private final CachePort cache;
    private final List<UrlRiskCheckerStrategy> urlRiskCheckerStrategies;

    public Mono<Risk> evaluateUrls(List<String> urls) {

        return Flux.fromIterable(urls)
                .flatMap(this::evaluateUrl)
                .filter(risk -> risk == Risk.MALICIOUS)
                .next()
                .defaultIfEmpty(Risk.SAFE);
    }

    private Mono<Risk> evaluateUrl(String raw) {

        String normalized = UrlUtils.normalize(raw);
        String host = UrlUtils.hostOf(normalized);
        String e2ld = UrlUtils.eTldPlusOne(host);
        String urlKey = Integer.toHexString(normalized.hashCode());

        return cache.getUrl(urlKey)
                .switchIfEmpty(cache.getETldPlusOne(e2ld))
                .switchIfEmpty(heuristicOrApi(normalized)
                    .flatMap(risk -> cache.putUrl(urlKey, risk, cacheTimeToLive(risk))
                            .then(cache.putETldPlusOne(e2ld, risk, cacheTimeToLive(risk)))
                            .thenReturn(risk)
                    )
                );
    }

    private Mono<Risk> heuristicOrApi(String url){

        boolean suspiciousUrl = urlRiskCheckerStrategies.stream().anyMatch(strategy -> strategy.breaksRule(url));

        if (!suspiciousUrl){

            return Mono.just(Risk.SAFE);
        }

        return webRisk.evaluate(url).onErrorResume(error -> Mono.just(Risk.SAFE));
    }

    private long cacheTimeToLive(Risk risk){

        return switch (risk) {
            case SAFE -> Duration.ofHours(48).toSeconds();
            case MALICIOUS -> Duration.ofDays(31).toSeconds();
        };
    }
}