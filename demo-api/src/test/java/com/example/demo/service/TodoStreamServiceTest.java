package com.example.demo.service;

import com.example.demo.model.Todo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import reactor.core.publisher.Sinks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unchecked")
class TodoStreamServiceTest {
    @InjectMocks
    private TodoStreamService todoStreamService;

    @Mock
    private Sinks.Many<Message<Todo>> many;

    @Test
    void sendMessage() {
        Todo todo = new Todo();

        todoStreamService.sendMessage(todo);

        ArgumentCaptor<Message<Todo>> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        verify(many).emitNext(messageArgumentCaptor.capture(), eq(Sinks.EmitFailureHandler.FAIL_FAST));
        assertThat(messageArgumentCaptor.getValue().getPayload()).isSameAs(todo);
    }
}