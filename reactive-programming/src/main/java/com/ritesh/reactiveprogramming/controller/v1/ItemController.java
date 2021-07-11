package com.ritesh.reactiveprogramming.controller.v1;

import com.ritesh.reactiveprogramming.controller.constants.ItemConstant;
import com.ritesh.reactiveprogramming.document.Item;
import com.ritesh.reactiveprogramming.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@RestController
public class ItemController {

    @Autowired
    private ItemReactiveRepository itemReactiveRepository;

    @GetMapping(value = ItemConstant.ITEM_END_POINT_V1, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Item> getAllItems() {
        return itemReactiveRepository.findAll().delayElements(Duration.ofSeconds(5));
    }

    @GetMapping(ItemConstant.ITEM_END_POINT_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> getOneItem(@PathVariable("id") String id) {
        return itemReactiveRepository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(ItemConstant.ITEM_END_POINT_V1)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item) {
        return itemReactiveRepository.save(item);
    }

    @DeleteMapping(ItemConstant.ITEM_END_POINT_V1 + "/{id}")
    public Mono<Void> deleteItem(@PathVariable("id") String id) {
        return itemReactiveRepository.deleteById(id);
    }

    @PutMapping(ItemConstant.ITEM_END_POINT_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable("id") String id, @RequestBody Item item) {

        return itemReactiveRepository.findById(id)
                .flatMap(currentItem -> {
                    currentItem.setPrice(item.getPrice());
                    currentItem.setDescription(item.getDescription());
                    return itemReactiveRepository.save(currentItem);
                })
                .map(updateItem -> new ResponseEntity<>(updateItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(ItemConstant.ITEM_END_POINT_V1+"/runtimeException")
    public Flux<Item> runtimeException(){
        return itemReactiveRepository.findAll().concatWith(Mono.error(new RuntimeException("Runtime Exception Occurred.")));
    }
}
