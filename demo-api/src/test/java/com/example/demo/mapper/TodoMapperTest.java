package com.example.demo.mapper;

import com.example.demo.domain.TodoEntity;
import com.example.demo.model.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TodoMapperTest {

    private static final String ID = "id-79";
    private static final String LABEL = "Lorem ipsum";
    private static final String DESCRIPTION = "Another description";
    private TodoMapper todoMapper;

    @BeforeEach
    void setUp() {
        todoMapper = Mappers.getMapper(TodoMapper.class);
    }

    @Test
    void toEntity() {

        assertThat(todoMapper.toEntity(buildTodo()))
                .usingRecursiveComparison()
                .isEqualTo(buildTodoEntity());
    }

    @Test
    void fromEntity() {
        assertThat(todoMapper.fromEntity(buildTodoEntity()))
                .usingRecursiveComparison()
                .isEqualTo(buildTodo());
    }

    private Todo buildTodo() {
        return Todo.builder()
                .id(ID)
                .label(LABEL)
                .description(DESCRIPTION)
                .build();
    }

    private TodoEntity buildTodoEntity() {
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setId(ID);
        todoEntity.setLabel(LABEL);
        todoEntity.setDescription(DESCRIPTION);
        return todoEntity;
    }
}