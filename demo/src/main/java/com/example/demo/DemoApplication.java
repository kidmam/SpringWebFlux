package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
@Slf4j
public class DemoApplication {

    /*@RestController
    public static class MyController {

    }*/

    @GetMapping("/")
    Mono<String> hello() {
        log.info("pos1");

        Mono<String> m = Mono.just(generateHello()).doOnNext( c -> log.info(c)).log(); //Publisher -> (Publisher) -> (Publisher) -> Subscriber
        //String msg2 = m.block();

        //Mono m = Mono.fromSupplier( () -> generateHello()).doOnNext( c -> log.info(c)).log();
        //m.subscribe();

        log.info("pos2: ");
        return m;
        //return Mono.just(msg2);
    }

    private String generateHello() {
        log.info("method generateHello()");
        return "Hello, WebFlux";
    }

    public static void main(String[] args) {
        System.setProperty("server.port", "8081");
        SpringApplication.run(DemoApplication.class, args);
    }
}
