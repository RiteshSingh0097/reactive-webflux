package com.ritesh.reactiveprogramming.initializer;

import com.ritesh.reactiveprogramming.document.Item;
import com.ritesh.reactiveprogramming.document.ItemCapped;
import com.ritesh.reactiveprogramming.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
@EnableAutoConfiguration
public class ItemDataInitializer implements CommandLineRunner {

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    /*@Autowired
    private MongoOperations mongoOperations;*/


    @Override
    public void run(String... args) {
        initialDataSetup();
        //createCappedCollection();
    }

   /* private void createCappedCollection() {
        mongoOperations.dropCollection(ItemCapped.class);
        mongoOperations.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped());
    }*/

    public List<Item> data() {
        return Arrays.asList(new Item(null, "Samsung TV", 399.99),
                new Item(null, "LG TV", 329.99),
                new Item(null, "Apple Watch", 349.99),
                new Item("ABC", "Beats HeadPhone", 19.99));
    }

    /*public void dataSetUpForCappedCollection() {
        Flux.interval(Duration.ofSeconds(1))
                .map(i -> new ItemCapped(null, "Random Item " + i, (100.00 + i)));
    }*/

    private void initialDataSetup() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .thenMany(itemReactiveRepository.findAll())
                .subscribe(item -> System.out.println("Item inserted from CommandLineRunner : " + item));
    }
}
