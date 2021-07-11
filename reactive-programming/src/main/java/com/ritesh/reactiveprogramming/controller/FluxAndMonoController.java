package com.ritesh.reactiveprogramming.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FluxAndMonoController {

    @GetMapping("/flux")
    public Flux<Integer> returnFlux() {
        return Flux.just(1, 2, 3, 4)
                .delayElements(Duration.ofSeconds(10))
                .log();
    }

    @GetMapping(value = "/fluxstream", produces = "application/stream+json")
    public Flux<Long> returnFluxStream() {
        return Flux.interval(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping("/mono")
    public Mono<Integer> returnMono() {
        return Mono.just(1)
                .log();
    }
}
