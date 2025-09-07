package io.github.g4lowy.phishingguard.riskdetection.adapter.out.cache;


import io.github.g4lowy.phishingguard.riskdetection.application.port.out.CachePort;
import io.github.g4lowy.phishingguard.riskdetection.domain.model.Risk;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
class RedisCacheAdapter implements CachePort {

    private final ReactiveStringRedisTemplate redisTemplate;

    @Override
    public Mono<Risk> getUrl(String key)  {

        return redisTemplate.opsForValue()
                .get("risk:url:" + key)
                .map(Risk::valueOf);
    }

    @Override
    public Mono<Risk> getETldPlusOne(String e2ld){

        return redisTemplate.opsForValue()
                .get("risk:e2ld:" + e2ld)
                .map(Risk::valueOf);
    }

    @Override
    public Mono<Void> putUrl(String key, Risk risk) {

        return redisTemplate.opsForValue()
                .set("risk:url:" + key, risk.name(), Duration.ofSeconds(cacheTimeToLive(risk)))
                .then();
    }

    @Override
    public Mono<Void> putETldPlusOne(String e2ld, Risk risk) {

        return redisTemplate.opsForValue()
                .set("risk:e2ld:"+e2ld, risk.name(), Duration.ofSeconds(cacheTimeToLive(risk)))
                .then();
    }

    private long cacheTimeToLive(Risk risk) {

        return switch (risk) {
            case SAFE -> Duration.ofHours(48).toSeconds();
            case SUSPICIOUS -> Duration.ofDays(7).toSeconds();
            case MALICIOUS -> Duration.ofDays(31).toSeconds();
        };
    }
}
