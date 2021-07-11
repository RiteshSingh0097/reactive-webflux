package com.ritesh.reactiveprogramming.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

class VirtualTimeTest {

    @Test
    void testingWithoutVirtualTime() {

        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1))
                .take(3);

        StepVerifier.create(longFlux.log())
                .expectSubscription()
                .expectNext(0l, 1l, 2l)
                .verifyComplete();
    }

    @Test
    void testingWithVirtualTime() {

        VirtualTimeScheduler.getOrSet();
        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1))
                .take(3);

        StepVerifier.withVirtualTime(longFlux::log)
                .expectSubscription()
                .expectNext(0l, 1l, 2l)
                .verifyComplete();
    }
}
