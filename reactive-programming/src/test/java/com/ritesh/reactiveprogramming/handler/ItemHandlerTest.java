package com.ritesh.reactiveprogramming.handler;

import com.ritesh.reactiveprogramming.controller.constants.ItemConstant;
import com.ritesh.reactiveprogramming.document.Item;
import com.ritesh.reactiveprogramming.repository.ItemReactiveRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
public class ItemHandlerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Test
    public void getAllItems() {
        webTestClient.get().uri(ItemConstant.ITEM_FUN_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBodyList(Item.class)
                .hasSize(4);
    }

    @Test
    public void getAllItemsApproach2() {
        webTestClient.get().uri(ItemConstant.ITEM_FUN_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBodyList(Item.class)
                .hasSize(4)
                .consumeWith(response -> {
                    List<Item> items = response.getResponseBody();
                    assert items != null;
                    items.forEach(item -> assertNotNull(item.getId()));
                });
    }

    @Test
    public void getAllItemsApproach3() {
        Flux<Item> itemFlux = webTestClient.get().uri(ItemConstant.ITEM_FUN_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .returnResult(Item.class)
                .getResponseBody();

        StepVerifier.create(itemFlux.log())
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void getOneItem() {
        webTestClient.get().uri(ItemConstant.ITEM_FUN_END_POINT_V1.concat("/{id}"), "ABC")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 19.99);
    }

    @Test
    public void getOneItemNotFound() {
        webTestClient.get().uri(ItemConstant.ITEM_FUN_END_POINT_V1.concat("/{id}"), "DEF")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void createItem() {
        Item item = new Item(null, "Iphone X", 999.99);
        webTestClient.post().uri(ItemConstant.ITEM_FUN_END_POINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Iphone X")
                .jsonPath("$.price").isEqualTo(999.99);
    }

    @Test
    public void deleteItem() {
        webTestClient.delete().uri(ItemConstant.ITEM_FUN_END_POINT_V1.concat("/{id}"), "ABC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void updateItem() {
        Item item = new Item(null, "Iphone X", 999.99);
        webTestClient.put().uri(ItemConstant.ITEM_FUN_END_POINT_V1.concat("/{id}"), "ABC")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("ABC")
                .jsonPath("$.description").isEqualTo("Iphone X")
                .jsonPath("$.price").isEqualTo(999.99);
    }

    @Test
    public void runTimeException() {

        webTestClient.get().uri("/v1/fun/runtimeexception")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.message", 500);
    }
}
