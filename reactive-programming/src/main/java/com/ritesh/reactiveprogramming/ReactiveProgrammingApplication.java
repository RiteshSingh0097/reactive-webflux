package com.ritesh.reactiveprogramming;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Slf4j
@EnableMongoRepositories
@SpringBootApplication
public class ReactiveProgrammingApplication {
    public static void main(String[] args) {
        log.info("processor::" + Runtime.getRuntime().availableProcessors());
        SpringApplication.run(ReactiveProgrammingApplication.class, args);
    }

}
