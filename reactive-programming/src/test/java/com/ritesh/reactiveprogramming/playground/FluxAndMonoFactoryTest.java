package com.ritesh.reactiveprogramming.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

class FluxAndMonoFactoryTest {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");
    String[] namesArray = new String[]{"adam", "anna", "jack", "jenny"};

    @Test
    void fluxUsingIterable() {
        Flux<String> namesFlux = Flux.fromIterable(names).log();
        StepVerifier.create(namesFlux)
                .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete();
    }

    @Test
    void fluxUsingArray() {
        Flux<String> namesFlux = Flux.fromArray(namesArray).log();
        StepVerifier.create(namesFlux)
                .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete();

    }

    @Test
    void fluxUsingStream() {
        Flux<String> namesFlux = Flux.fromStream(names.stream()).log();
        StepVerifier.create(namesFlux)
                .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete();
    }

    @Test
    void fluxUsingRange(){
        Flux<Integer> integerFlux = Flux.range(1, 5);
        StepVerifier.create(integerFlux)
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }

    @Test
    void monoUsingJustOrEmpty() {
        Mono<String> mono = Mono.justOrEmpty(null);
        StepVerifier.create(mono.log())
                .verifyComplete();
    }

    @Test
    void monoUsingSupplier() {
        Supplier<String> stringSupplier = () -> "adam";
        Mono<String> stringMono = Mono.fromSupplier(stringSupplier);
        System.out.println(stringSupplier.get());
        StepVerifier.create(stringMono.log())
                .expectNext("adam")
                .verifyComplete();
    }
}
