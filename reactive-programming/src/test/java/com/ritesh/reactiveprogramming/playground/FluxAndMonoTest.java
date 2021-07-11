package com.ritesh.reactiveprogramming.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTest {

    @Test
    void fluxTest() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                //         .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("After Error"))
                .log();
        stringFlux.subscribe(System.out::println, System.err::println, () -> System.out.println("Completed"));
    }

    @Test
    void fluxTestElementsWithoutError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .verifyComplete();

    }

    @Test
    void fluxTestElementsWithError() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactive Spring")
                .expectError(RuntimeException.class)
                //.expectErrorMessage("Exception Occurred")
                .verify();
    }

    @Test
    void fluxTestElementsCount() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                .subscribeOn(parallel()) //run parallel
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void monoTest() {
        Mono<String> mono = Mono.just("Spring");
        StepVerifier.create(mono.log())
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    void monoTestWithError() {
        StepVerifier.create(Mono.error(new RuntimeException("Exception")).log())
                .expectErrorMessage("Exception")
                .verify();
    }
}
