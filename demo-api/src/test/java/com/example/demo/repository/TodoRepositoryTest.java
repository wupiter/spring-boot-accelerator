package com.example.demo.repository;

import com.example.demo.domain.TodoEntity;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DataJpaTest
class TodoRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void findByLabel() {
        TodoEntity todoEntity1 = buildTodoEntity("Label 20", "Description 04");
        entityManager.persist(todoEntity1);
        TodoEntity todoEntity2 = buildTodoEntity("Label 19", "Description 08");
        entityManager.persist(todoEntity2);
        TodoEntity todoEntity3 = buildTodoEntity("My Label", "Description 14");
        entityManager.persist(todoEntity3);

        entityManager.flush();

        Pageable pageable = PageRequest.of(0, 2);

        assertThat(todoRepository.findByLabel("Label", pageable))
                .usingRecursiveComparison()
                .isEqualTo(new PageImpl<>(Lists.list(todoEntity1, todoEntity2), pageable, 2));
    }

    private TodoEntity buildTodoEntity(String label, String description) {
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setLabel(label);
        todoEntity.setDescription(description);
        return todoEntity;
    }
}