package com.example.demo.com.example.flux;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@Slf4j
@RestController
public class FluxApplication {

    /*@GetMapping("/event/{id}") //단수 Data
    Mono<Event> event(@PathVariable long id) {
        return Mono.just(new Event(id, "event" + id));
    }*/

    @GetMapping("/event/{id}") //단수 Data
    Mono<List<Event>> event(@PathVariable long id) {
        List<Event> list = Arrays.asList(new Event(1, "event1"), new Event(2, "event2"));
        return Mono.just(list);
    }

    /*@GetMapping("/events") //복수 Data
    Flux<Event> events() {
        return Flux.just(new Event(1, "event1"), new Event(2, "event2"));
    }*/

    /*@GetMapping("/events") //복수 Data
    Flux<Event> events() {
        List<Event> list = Arrays.asList(new Event(1, "event1"), new Event(2, "event2"));
        return Flux.fromIterable(list);
    }*/

    /*@GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> events() {
        List<Event> list = Arrays.asList(new Event(1, "event1"), new Event(2, "event2"));
        return Flux.fromIterable(list);
    }*/

    /*@GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> events() {
        return Flux
                //.fromStream(Stream.generate( () -> new Event(System.currentTimeMillis(), "event") ))
                //.<Event>generate(sink -> sink.next(new Event(System.currentTimeMillis(), "value")))
                .<Event, Long>generate( () -> 1L, (id, sink) -> {
                    sink.next(new Event(id, "value" + id));
                    return id + 1;
                })
                .delayElements(Duration.ofSeconds(1))
                .take(10);
    }*/

    /*@GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> events() {
        Flux<Event> es =  Flux
                .<Event, Long>generate( () -> 1L, (id, sink) -> {
                    sink.next(new Event(id, "value" + id));
                    return id + 1;
                });
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        return Flux.zip(es, interval).map(tu -> tu.getT1());
    }*/

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Event> events() {
        Flux<String> es =  Flux.generate(sink -> sink.next("value"));
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));

        return Flux.zip(es, interval).map(tu -> new Event(tu.getT2() + 1, tu.getT1())).take(10);
    }

    public static void main(String[] args) {
        System.setProperty("server.port", "8082");
        SpringApplication.run( FluxApplication.class, args);
    }

    @Data
    @AllArgsConstructor
    public static class Event {
        long id;
        String value;
        /*@JsonIgnore
        String nonJson;*/
    }
}
