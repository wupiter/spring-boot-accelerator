package com.example.demo.service;

import com.example.demo.domain.TodoEntity;
import com.example.demo.error.NotFoundApiException;
import com.example.demo.mapper.TodoMapper;
import com.example.demo.model.Todo;
import com.example.demo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    private static final String ID = "id-19";
    private static final String LABEL = "Label 1";
    private static final String DESCRIPTION = "Description";

    private static final Todo TODO = new Todo();
    private static final TodoEntity TODO_ENTITY = new TodoEntity();

    @InjectMocks
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;
    @Mock
    private TodoMapper todoMapper;

    @Test
    void createTodo() {
        Todo todo = Todo.builder()
                .label(LABEL)
                .description(DESCRIPTION)
                .build();

        when(todoMapper.toEntity(todo)).thenReturn(TODO_ENTITY);
        when(todoRepository.save(TODO_ENTITY)).thenReturn(TODO_ENTITY);
        when(todoMapper.fromEntity(TODO_ENTITY)).thenReturn(TODO);

        assertThat(todoService.createTodo(todo))
                .isSameAs(TODO);
    }

    @Test
    void getById_Found() {
        when(todoRepository.findById(ID)).thenReturn(Optional.of(TODO_ENTITY));
        when(todoMapper.fromEntity(TODO_ENTITY)).thenReturn(TODO);

        assertThat(todoService.getById(ID))
                .isSameAs(TODO);
    }

    @Test
    void getById_NotFound() {
        when(todoRepository.findById(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.getById(ID))
                .isInstanceOf(NotFoundApiException.class)
                .hasMessage("No todo found with id: id-19");
    }

    @Test
    void findAll() {
        PageRequest pageable = PageRequest.of(0, 1);
        PageImpl<TodoEntity> page = new PageImpl<>(Collections.singletonList(TODO_ENTITY), pageable, 2);

        when(todoRepository.findAll(pageable)).thenReturn(page);
        when(todoMapper.fromEntity(TODO_ENTITY)).thenReturn(TODO);

        assertThat(todoService.findAll(pageable))
                .usingRecursiveComparison()
                .isEqualTo(new PageImpl<>(Collections.singletonList(TODO), pageable, 2));
    }

    @Test
    void findByLabel() {
        String label = "My Label";
        PageRequest pageable = PageRequest.of(0, 1);
        PageImpl<TodoEntity> page = new PageImpl<>(Collections.singletonList(TODO_ENTITY), pageable, 2);

        when(todoRepository.findByLabel(label, pageable)).thenReturn(page);
        when(todoMapper.fromEntity(TODO_ENTITY)).thenReturn(TODO);

        assertThat(todoService.findByLabel(label, pageable))
                .usingRecursiveComparison()
                .isEqualTo(new PageImpl<>(Collections.singletonList(TODO), pageable, 2));
    }

    @Test
    void deleteById() {
        todoService.deleteById(ID);

        verify(todoRepository).deleteById(ID);
    }
}