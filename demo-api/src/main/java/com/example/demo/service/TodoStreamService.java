package com.example.demo.service;

import com.example.demo.model.Todo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoStreamService {
    private final Sinks.Many<Message<Todo>> many;

    public void sendMessage(Todo todo) {
        Message<Todo> message = MessageBuilder.withPayload(todo).build();
        many.emitNext(message, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    public void onMessage(Message<Todo> msg) {
        log.info("Received todo message: {}", msg.getPayload());
    }
}
