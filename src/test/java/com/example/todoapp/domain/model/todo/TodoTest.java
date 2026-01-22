package com.example.todoapp.domain.model.todo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class TodoTest {

    @Test
    void create_新規Todoが正しい初期状態で生成される() {
        // arrange
        LocalDateTime before = LocalDateTime.now();
        LocalDate dueDate = LocalDate.of(2099, 1, 1);

        // act
        Todo todo = Todo.create("タイトル", "詳細", dueDate);
        LocalDateTime after = LocalDateTime.now();

        // assert
        // internalId は null
        assertNull(todo.getInternalId());

        // publicId が生成される
        assertNotNull(todo.getPublicId());

        // versionNumber = 1
        assertEquals(1, todo.getVersionNumber().value());

        // completed = false
        assertFalse(todo.isCompleted());

        // deleted = false
        assertFalse(todo.isDeleted());

        // createdAt が「before ～ after」の範囲にある
        assertFalse(todo.getCreatedAt().isBefore(before));
        assertFalse(todo.getCreatedAt().isAfter(after));

        // updatedAt = createdAt
        assertEquals(todo.getCreatedAt(), todo.getUpdatedAt());

        // dueDate が設定される
        assertEquals(dueDate, todo.getDueDate().value());

        // title が設定される
        assertEquals("タイトル", todo.getTitle());

        // detail が設定される
        assertEquals("詳細", todo.getDetail());
    }

    @Test
    void create_タイトルがnullの場合は例外を投げる() {
        LocalDate dueDate = LocalDate.now().plusDays(1);

        assertThrows(IllegalArgumentException.class, () -> {
            Todo.create(null, "詳細", dueDate);
        });
    }

    @Test
    void create_タイトルが空文字の場合は例外を投げる() {
        LocalDate dueDate = LocalDate.now().plusDays(1);

        assertThrows(IllegalArgumentException.class, () -> {
            Todo.create("", "詳細", dueDate);
        });
    }

    @Test
    void create_タイトルが空白のみの場合は例外を投げる() {
        LocalDate dueDate = LocalDate.now().plusDays(1);

        assertThrows(IllegalArgumentException.class, () -> {
            Todo.create("   ", "詳細", dueDate);
        });
    }

    @Test
    void create_期限日がnullの場合は例外を投げる() {
        assertThrows(IllegalArgumentException.class, () -> {
            Todo.create("タイトル", "詳細", null);
        });
    }

    @Test
    void create_期限日が過去日の場合は例外を投げる() {
        LocalDate pastDate = LocalDate.now().minusDays(1);

        assertThrows(IllegalArgumentException.class, () -> {
            Todo.create("タイトル", "詳細", pastDate);
        });
    }

}
