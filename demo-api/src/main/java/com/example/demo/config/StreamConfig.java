package com.example.demo.config;

import com.example.demo.model.Todo;
import com.example.demo.service.TodoStreamService;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
@Slf4j
public class StreamConfig {
    @Bean
    public Sinks.Many<Message<Todo>> many() {
        return Sinks.many().unicast().onBackpressureBuffer();
    }

    @Bean
    public Supplier<Flux<Message<Todo>>> supply(Sinks.Many<Message<Todo>> many) {
        return () -> many.asFlux()
                .doOnNext(msg -> log.info("Sending message: {}", msg))
                .doOnError(e -> log.error("Failed Sending message", e));
    }

    @Bean
    public Consumer<Message<Todo>> consume(TodoStreamService todoStreamService) {
        return todoStreamService::onMessage;
    }
}
