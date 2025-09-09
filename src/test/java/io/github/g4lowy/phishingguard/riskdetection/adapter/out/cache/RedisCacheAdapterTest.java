package io.github.g4lowy.phishingguard.riskdetection.adapter.out.cache;

import io.github.g4lowy.phishingguard.riskdetection.domain.model.Risk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class RedisCacheAdapterTest {

    private ReactiveValueOperations<String,String> valueOps;
    private RedisCacheAdapter redisCacheAdapter;

    @BeforeEach
    void setUp() {
        ReactiveStringRedisTemplate redisTemplate = Mockito.mock(ReactiveStringRedisTemplate.class);
        valueOps = Mockito.mock(ReactiveValueOperations.class);

        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOps);

        redisCacheAdapter = new RedisCacheAdapter(redisTemplate);
    }

    @Test
    void getUrl_shouldReturnRisk_whenValueExists() {
        Mockito.when(valueOps.get("risk:url:test"))
                .thenReturn(Mono.just("SAFE"));

        StepVerifier.create(redisCacheAdapter.getUrl("test"))
                .expectNext(Risk.SAFE)
                .verifyComplete();
    }

    @Test
    void getUrl_shouldCompleteEmpty_whenValueDoesNotExist() {
        Mockito.when(valueOps.get("risk:url:missing"))
                .thenReturn(Mono.empty());

        StepVerifier.create(redisCacheAdapter.getUrl("missing"))
                .verifyComplete();
    }

    @Test
    void getETldPlusOne_shouldReturnRisk_whenValueExists() {
        Mockito.when(valueOps.get("risk:e2ld:example.com"))
                .thenReturn(Mono.just("MALICIOUS"));

        StepVerifier.create(redisCacheAdapter.getETldPlusOne("example.com"))
                .expectNext(Risk.MALICIOUS)
                .verifyComplete();
    }

    @Test
    void putUrl_shouldStoreWithCorrectTTL() {
        Mockito.when(valueOps.set(eq("risk:url:test"), eq("SUSPICIOUS"), any(Duration.class)))
                .thenReturn(Mono.just(true));

        StepVerifier.create(redisCacheAdapter.putUrl("test", Risk.SUSPICIOUS))
                .verifyComplete();

        Mockito.verify(valueOps)
                .set("risk:url:test", "SUSPICIOUS", Duration.ofDays(7));
    }

    @Test
    void putETldPlusOne_shouldStoreWithCorrectTTL() {
        Mockito.when(valueOps.set(eq("risk:e2ld:example.com"), eq("MALICIOUS"), any(Duration.class)))
                .thenReturn(Mono.just(true));

        StepVerifier.create(redisCacheAdapter.putETldPlusOne("example.com", Risk.MALICIOUS))
                .verifyComplete();

        Mockito.verify(valueOps)
                .set("risk:e2ld:example.com", "MALICIOUS", Duration.ofDays(31));
    }
}
