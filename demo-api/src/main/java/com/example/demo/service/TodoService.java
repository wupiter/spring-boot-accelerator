package com.example.demo.service;

import com.example.demo.domain.TodoEntity;
import com.example.demo.error.NotFoundApiException;
import com.example.demo.mapper.TodoMapper;
import com.example.demo.model.Todo;
import com.example.demo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
@PreAuthorize("hasRole('user')")
public class TodoService {
    private final TodoMapper todoMapper;
    private final TodoRepository todoRepository;

    public Todo createTodo(Todo todo) {
        TodoEntity todoEntity = todoMapper.toEntity(todo);
        todoRepository.save(todoEntity);
        return todoMapper.fromEntity(todoEntity);
    }

    @Transactional(readOnly = true)
    public Todo getById(String id) {
        return todoRepository.findById(id)
                .map(todoMapper::fromEntity)
                .orElseThrow(() -> new NotFoundApiException("No todo found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Todo> findAll(Pageable pageable) {
        return todoRepository.findAll(pageable)
                .map(todoMapper::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<Todo> findByLabel(String label, Pageable pageable) {
        return todoRepository.findByLabel(label, pageable)
                .map(todoMapper::fromEntity);
    }

    public void deleteById(String todoId) {
        todoRepository.deleteById(todoId);
    }
}
