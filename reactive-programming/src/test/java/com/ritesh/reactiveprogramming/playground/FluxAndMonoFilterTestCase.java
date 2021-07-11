package com.ritesh.reactiveprogramming.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTestCase {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    void filterTest(){
        Flux<String> namesFlux = Flux.fromIterable(names)
                .filter(s -> s.startsWith("a"))
                .log();

        StepVerifier.create(namesFlux)
                .expectNext("adam","anna")
                .verifyComplete();
    }
}
