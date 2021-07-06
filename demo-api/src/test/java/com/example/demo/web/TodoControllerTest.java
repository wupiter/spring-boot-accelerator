package com.example.demo.web;

import com.example.demo.error.NotFoundApiException;
import com.example.demo.model.Todo;
import com.example.demo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
//{{#if (eval authentication '==' 'keycloak-jwt')}}
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
//{{/if}}
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
//{{#if (eval authentication '==' 'keycloak-jwt')}}
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//{{/if}}
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TodoController.class)
//{{#if (eval authentication '==' 'keycloak-jwt')}}
@WithMockUser(username = "john", roles = { "user" })
//{{/if}}
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Test
    void create_Success() throws Exception {
        when(todoService.createTodo(isA(Todo.class))).thenAnswer(answer -> {
            Todo todo = answer.getArgument(0);
            todo.setId(UUID.randomUUID().toString());
            return todo;
        });

        mockMvc.perform(post("/api/v1/todos")
                .content("{\"label\": \"My todo\", \"description\": \"My description\"}")
                .contentType(MediaType.APPLICATION_JSON)
                //{{#if (eval authentication '==' 'keycloak-jwt')}}
                .with(csrf())
                //{{/if}}
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.label").value("My todo"))
                .andExpect(jsonPath("$.description").value("My description"))
        ;
    }

    //{{#if (eval authentication '==' 'keycloak-jwt')}}
    @Test
    @WithAnonymousUser
    void create_MissingUserRole() throws Exception {
        when(todoService.createTodo(isA(Todo.class))).thenAnswer(answer -> {
            Todo todo = answer.getArgument(0);
            todo.setId(UUID.randomUUID().toString());
            return todo;
        });

        mockMvc.perform(post("/api/v1/todos")
                .content("{\"label\": \"My todo\", \"description\": \"My description\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                )
                .andExpect(status().isUnauthorized())
        ;
    }
    //{{/if}}

    @Test
    void create_MissingLabel() throws Exception {
        mockMvc.perform(post("/api/v1/todos")
                .content("{\"description\": \"My description\"}")
                .contentType(MediaType.APPLICATION_JSON)
                //{{#if (eval authentication '==' 'keycloak-jwt')}}
                .with(csrf())
                //{{/if}}
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The label must not be null"))
        ;
    }

    @Test
    void create_TooLongLabel() throws Exception {
        mockMvc.perform(post("/api/v1/todos")
                .content("{\"label\": \"My todo is too loooooooooooooooong\", \"description\": \"My description\"}")
                .contentType(MediaType.APPLICATION_JSON)
                //{{#if (eval authentication '==' 'keycloak-jwt')}}
                .with(csrf())
                //{{/if}}
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The label size must be between 1 and 20"))
        ;
    }

    @Test
    void getById_Success() throws Exception {
        Todo todo = createTodo();

        when(todoService.getById("id-123")).thenReturn(todo);

        mockMvc.perform(get("/api/v1/todos/id-123")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("id-123"))
                .andExpect(jsonPath("$.label").value("My todo"))
                .andExpect(jsonPath("$.description").value("This is a description"))
        ;
    }

    private Todo createTodo() {
        return Todo.builder()
                .id("id-123")
                .label("My todo")
                .description("This is a description")
                .build();
    }

    @Test
    void getById_NotFound() throws Exception {
        when(todoService.getById("id-not-exists")).thenThrow(new NotFoundApiException("Todo not found"));

        mockMvc.perform(get("/api/v1/todos/id-not-exists")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Todo not found"))
        ;
    }

    @Test
    void findAll() throws Exception {
        Todo todo = createTodo();
        PageRequest pageable = buildPageable();
        Page<Todo> page = buildPage(todo, pageable);

        when(todoService.findAll(pageable)).thenReturn(page);

        mockMvc.perform(get("/api/v1/todos?page=0&size=1&sort=label&label.dir=asc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value("id-123"))
                .andExpect(jsonPath("$.content[0].label").value("My todo"))
                .andExpect(jsonPath("$.content[0].description").value("This is a description"))
                .andExpect(jsonPath("$.pageable.sort.sorted").value(true))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
        ;
    }

    private PageImpl<Todo> buildPage(Todo todo, PageRequest pageable) {
        return new PageImpl<>(Collections.singletonList(todo), pageable, 2);
    }

    private PageRequest buildPageable() {
        return PageRequest.of(0, 1, Sort.Direction.ASC, "label");
    }

    @Test
    void findByLabel() throws Exception {
        Todo todo = createTodo();
        PageRequest pageable = buildPageable();
        Page<Todo> page = buildPage(todo, pageable);

        when(todoService.findByLabel("Label", pageable)).thenReturn(page);

        mockMvc.perform(get("/api/v1/todos?label=Label&page=0&size=1&sort=label&label.dir=asc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value("id-123"))
                .andExpect(jsonPath("$.content[0].label").value("My todo"))
                .andExpect(jsonPath("$.content[0].description").value("This is a description"))
                .andExpect(jsonPath("$.pageable.sort.sorted").value(true))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
        ;
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(delete("/api/v1/todos/id-123")
                //{{#if (eval authentication '==' 'keycloak-jwt')}}
                .with(csrf())
                //{{/if}}
                )
                .andExpect(status().isAccepted())
        ;

        verify(todoService).deleteById("id-123");
    }
}