package com.ritesh.reactiveprogramming.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

class FluxAndMonoTransformTest {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    void transformUsingMap() {
        Flux<Integer> namesFlux = Flux.fromIterable(names)
                .map(String::length)
                .log();

        StepVerifier.create(namesFlux)
                .expectNext(4, 4, 4, 5)
                .verifyComplete();
    }

    @Test
    void transformUsingFlatMapInParallelProcess() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .window(2)
                .flatMap((s) ->
                        s.map(this::convertToList).subscribeOn(parallel()))
                .flatMap(Flux::fromIterable)
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
           // Thread.sleep(4000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "ritesh");
    }
}
