package com.learnreactivespring.itemclient.controller;

import com.learnreactivespring.itemclient.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class ItemClientController {

    WebClient webClient = WebClient.create("http://localhost:8080");

    @GetMapping(value = "/client/retrieve", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Item> getAllItemsUsingRetrieve() {
        return webClient.get().uri("v1/items")
                .retrieve()
                .bodyToFlux(Item.class)
                .log("Items in Client Project retrieve : ");
    }

    @GetMapping("/client/exchange")
    public Flux<Item> getAllItemsUsingExchange() {
        return webClient.get().uri("v1/items")
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Item.class))
                .log("Items in Client Project exchange : ");
    }

    @GetMapping("/client/exchange/singleItem")
    public Mono<Item> getOneItemUsingRetrieve() {
        var id = "ABC";
        return webClient.get().uri("/v1/items/{id}", id)
                .exchangeToMono(clientResponse -> {
                    log.info("response status :: {}", clientResponse.statusCode());
                    log.info("response headers :: {}", clientResponse.headers());
                    return clientResponse.bodyToMono(Item.class);
                })
                .log("items in client project retrieve : ");
    }

    @PostMapping("/client/createItem")
    public Mono<Item> createItem(@RequestBody Item item) {

        Mono<Item> itemMono = Mono.just(item);
        return webClient.post().uri("/v1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemMono, Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("Created item is : ");
    }


    @PutMapping("/client/updateItem/{id}")
    public Mono<Item> updateItem(@PathVariable("id") String id,
                                 @RequestBody Item item) {

        Mono<Item> itemBody = Mono.just(item);
        return webClient.put().uri("/v1/items/{id}", id)
                .body(itemBody, Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("Updated Item is : ");

    }

    @DeleteMapping("/client/deleteItem/{id}")
    public Mono<Void> deleteItem(@PathVariable String id) {
        return webClient.delete().uri("/v1/items/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .log("Deleted item is : ");
    }

    @GetMapping("/client/retrieve/error")
    public Flux<Item> errorRetrieve() {
        return webClient.get()
                .uri("/v1/fun/runtimeexception")
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> {

                    Mono<String> errorMono = clientResponse.bodyToMono(String.class);
                    return errorMono.flatMap(errorMessage -> {
                        log.error("The error Message is : " + errorMessage);
                        return Mono.error(new RuntimeException(errorMessage));
                    });
                })
                .bodyToFlux(Item.class);
    }

    @GetMapping("/client/exchange/error")
    public Flux<Item> errorExchange() {
        return webClient.get()
                .uri("/v1/fun/runtimeexception")
                .exchange()
                .flatMapMany(clientResponse -> {
                    if (clientResponse.statusCode().is5xxServerError()) {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(errorMessage -> {
                                    log.error("Error Message is errorExchange : " + errorMessage);
                                    return Mono.error(new RuntimeException(errorMessage));
                                });
                    } else {
                        return clientResponse.bodyToFlux(Item.class);
                    }
                });
    }

}
