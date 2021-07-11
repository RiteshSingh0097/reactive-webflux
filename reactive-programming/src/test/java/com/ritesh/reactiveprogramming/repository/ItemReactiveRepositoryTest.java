package com.ritesh.reactiveprogramming.repository;

import com.ritesh.reactiveprogramming.document.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

//Todo: this will work only when host and port of mongodb will be comment out
@DataMongoTest
@DirtiesContext
@RunWith(SpringRunner.class)
public class ItemReactiveRepositoryTest {

    List<Item> itemList = Arrays.asList(new Item(null, "Samsung TV", 400.0),
            new Item(null, "LG TV", 420.0),
            new Item(null, "Apple Watch", 299.99),
            new Item(null, "Beats Headphones", 149.99),
            new Item("ABC", "Bose Headphones", 149.99));
    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    @Before
    public void setUp() {

        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("Inserted Item is ::" + item))
                .blockLast();
    }

    @Test
    public void getAllItems() {
        StepVerifier.create(itemReactiveRepository.findAll().log())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void getItemById() {
        StepVerifier.create(itemReactiveRepository.findById("ABC").log())
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Bose Headphones"))
                .verifyComplete();
    }

    @Test
    public void getItemByDescription() {
        StepVerifier.create(itemReactiveRepository.findByDescription("Bose Headphones").log("just chill"))
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Bose Headphones"))
                .verifyComplete();
    }

    @Test
    public void updateItem() {
        double newPrice = 520.00;
        Flux<Item> updatedItem = itemReactiveRepository.findByDescription("LG TV")
                .map(item -> {
                    item.setPrice(newPrice);
                    return item;
                })
                .flatMap(item -> itemReactiveRepository.save(item));

        StepVerifier.create(updatedItem.log())
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice() == newPrice)
                .verifyComplete();
    }

    @Test
    public void deleteItem() {
        Mono<Void> deletedItem = itemReactiveRepository.findById("ABC")
                .map(Item::getId)
                .flatMap(id -> itemReactiveRepository.deleteById(id));

        StepVerifier.create(deletedItem.log())
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll().log("Items:::"))
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }
}
