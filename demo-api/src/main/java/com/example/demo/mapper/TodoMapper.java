package com.example.demo.mapper;

import com.example.demo.domain.TodoEntity;
import com.example.demo.model.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TodoMapper {
    TodoEntity toEntity(Todo todo);
    Todo fromEntity(TodoEntity todo);
}
