package com.ritesh.reactiveprogramming.handler;

import com.ritesh.reactiveprogramming.document.Item;
import com.ritesh.reactiveprogramming.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ItemsHandler {

    static Mono<ServerResponse> notFound = ServerResponse.notFound().build();
    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    public Mono<ServerResponse> getAllItems(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemReactiveRepository.findAll(), Item.class);
    }

    public Mono<ServerResponse> getOneItem(ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");
        Mono<Item> itemMono = itemReactiveRepository.findById(id);
        return itemMono.flatMap(item ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(item)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        Mono<Item> itemToBeInserted = serverRequest.bodyToMono(Item.class);

        return itemToBeInserted.flatMap(item ->
                ServerResponse.created(serverRequest.uri())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(itemReactiveRepository.save(item), Item.class));
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {

        String id = serverRequest.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemReactiveRepository.deleteById(id), Void.class);
    }

    public Mono<ServerResponse> updateItem(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return serverRequest.bodyToMono(Item.class)
                .flatMap(item -> itemReactiveRepository.findById(id)
                        .flatMap(currentItem -> {
                            currentItem.setDescription(item.getDescription());
                            currentItem.setPrice(item.getPrice());
                            return itemReactiveRepository.save(currentItem);
                        })).flatMap(item -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(item)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> itemsEx(ServerRequest serverRequest) {
        throw new RuntimeException("Runtime Exception Occurred");
    }
}
