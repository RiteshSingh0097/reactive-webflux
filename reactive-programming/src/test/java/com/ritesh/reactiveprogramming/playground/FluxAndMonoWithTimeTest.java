package com.ritesh.reactiveprogramming.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

class FluxAndMonoWithTimeTest {

    @Test
    void infiniteSequenceTest() {
        Flux<Long> finiteFlux = Flux.interval(Duration.ofMillis(100))
                .take(3)
                .log();

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }

    @Test
    void infiniteSequenceMap() {
        Flux<Integer> finiteFlux = Flux.interval(Duration.ofMillis(300))
                //.delayElements(Duration.ofSeconds(1))
                .map(Long::intValue)
                .take(4)
                .log();

        StepVerifier.create(finiteFlux)
                .expectSubscription()
                .expectNext(0, 1, 2, 3)
                .verifyComplete();
    }
}
