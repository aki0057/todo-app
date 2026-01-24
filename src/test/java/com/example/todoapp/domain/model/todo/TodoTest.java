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

        // act & assert
        assertThatThrownBy(() -> Todo.create("タイトル", "詳細", pastDate))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ----------------------------------------------------------------------
    // update
    // ----------------------------------------------------------------------

    @Test
    void update_タイトル詳細期限日が更新され版数が増える() {
        // arrange
        LocalDateTime beforeUpdate = LocalDateTime.now();
        Todo todo = Todo.create("old title", "old detail", LocalDate.now().plusDays(1));

        // act
        todo.update("new title", "new detail", LocalDate.now().plusDays(2));

        // assert
        assertThat(todo.getTitle()).isEqualTo("new title");
        assertThat(todo.getDetail()).isEqualTo("new detail");
        assertThat(todo.getDueDate().value()).isEqualTo(LocalDate.now().plusDays(2));

        assertThat(todo.getUpdatedAt()).isAfterOrEqualTo(beforeUpdate);
        assertThat(todo.getVersionNumber().value()).isEqualTo(2);

        assertThat(todo.isCompleted()).isFalse();
        assertThat(todo.isDeleted()).isFalse();
    }

    @Test
    void update_タイトルがnullの場合は例外を投げる() {
        // arrange
        Todo todo = Todo.create("old title", "old detail", LocalDate.now().plusDays(1));

        // act & assert
        assertThatThrownBy(() -> todo.update(null, "new detail", LocalDate.now().plusDays(2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void update_タイトルが空文字の場合は例外を投げる() {
        // arrange
        Todo todo = Todo.create("old title", "old detail", LocalDate.now().plusDays(1));

        // act & assert
        assertThatThrownBy(() -> todo.update("", "new detail", LocalDate.now().plusDays(2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void update_タイトルが空白のみの場合は例外を投げる() {
        // arrange
        Todo todo = Todo.create("old title", "old detail", LocalDate.now().plusDays(1));

        // act & assert
        assertThatThrownBy(() -> todo.update("   ", "new detail", LocalDate.now().plusDays(2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void update_期限日がnullの場合は例外を投げる() {
        // arrange
        Todo todo = Todo.create("old title", "old detail", LocalDate.now().plusDays(1));

        // act & assert
        assertThatThrownBy(() -> todo.update("new title", "new detail", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void update_期限日が過去日の場合は例外を投げる() {
        // arrange
        Todo todo = Todo.create("old title", "old detail", LocalDate.now().plusDays(1));

        // act & assert
        assertThatThrownBy(() -> todo.update("new title", "new detail", LocalDate.now().minusDays(1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ----------------------------------------------------------------------
    // complete
    // ----------------------------------------------------------------------

    @Test
    void complete_未完了のTodoが完了状態になり版数と更新日時が更新される() {
        // arrange
        Todo todo = Todo.create("title", "detail", LocalDate.now().plusDays(1));
        LocalDateTime before = LocalDateTime.now();

        // act
        todo.complete();

        // assert
        assertThat(todo.isCompleted()).isTrue();
        assertThat(todo.getVersionNumber().value()).isEqualTo(2);
        assertThat(todo.getUpdatedAt()).isAfterOrEqualTo(before);
    }

    @Test
    void complete_すでに完了済みの場合は何も変わらない() {
        // arrange
        Todo todo = Todo.create("title", "detail", LocalDate.now().plusDays(1));
        todo.complete();
        LocalDateTime before = todo.getUpdatedAt();
        int versionBefore = todo.getVersionNumber().value();

        // act
        todo.complete();

        // assert
        assertThat(todo.isCompleted()).isTrue();
        assertThat(todo.getVersionNumber().value()).isEqualTo(versionBefore);
        assertThat(todo.getUpdatedAt()).isEqualTo(before);
    }

    @Test
    void complete_他の項目は変更されない() {
        // arrange
        LocalDate dueDate = LocalDate.now().plusDays(1);
        Todo todo = Todo.create("title", "detail", dueDate);

        InternalId internalIdBefore = todo.getInternalId();
        PublicId publicIdBefore = todo.getPublicId();
        LocalDateTime createdAtBefore = todo.getCreatedAt();
        String titleBefore = todo.getTitle();
        String detailBefore = todo.getDetail();
        DueDate dueDateBefore = todo.getDueDate();

        // act
        todo.complete();

        // assert
        assertThat(todo.getInternalId()).isEqualTo(internalIdBefore);
        assertThat(todo.getPublicId()).isEqualTo(publicIdBefore);
        assertThat(todo.getCreatedAt()).isEqualTo(createdAtBefore);
        assertThat(todo.getTitle()).isEqualTo(titleBefore);
        assertThat(todo.getDetail()).isEqualTo(detailBefore);
        assertThat(todo.getDueDate()).isEqualTo(dueDateBefore);
    }

    // ----------------------------------------------------------------------
    // delete
    // ----------------------------------------------------------------------

    @Test
    void delete_未削除のTodoが削除状態になり版数と更新日時が更新される() {
        // arrange
        Todo todo = Todo.create("title", "detail", LocalDate.now().plusDays(1));
        LocalDateTime before = LocalDateTime.now();

        // act
        todo.delete();

        // assert
        assertThat(todo.isDeleted()).isTrue();
        assertThat(todo.getVersionNumber().value()).isEqualTo(2);
        assertThat(todo.getUpdatedAt()).isAfterOrEqualTo(before);
    }

    @Test
    void delete_すでに削除済みの場合は何も変わらない() {
        // arrange
        Todo todo = Todo.create("title", "detail", LocalDate.now().plusDays(1));
        todo.delete();
        LocalDateTime before = todo.getUpdatedAt();
        int versionBefore = todo.getVersionNumber().value();

        // act
        todo.delete();

        // assert
        assertThat(todo.isDeleted()).isTrue();
        assertThat(todo.getVersionNumber().value()).isEqualTo(versionBefore);
        assertThat(todo.getUpdatedAt()).isEqualTo(before);
    }

    @Test
    void delete_他の項目は変更されない() {
        // arrange
        LocalDate dueDate = LocalDate.now().plusDays(1);
        Todo todo = Todo.create("title", "detail", dueDate);

        InternalId internalIdBefore = todo.getInternalId();
        PublicId publicIdBefore = todo.getPublicId();
        LocalDateTime createdAtBefore = todo.getCreatedAt();
        String titleBefore = todo.getTitle();
        String detailBefore = todo.getDetail();
        DueDate dueDateBefore = todo.getDueDate();
        boolean completedBefore = todo.isCompleted();

        // act
        todo.delete();

        // assert
        assertThat(todo.getInternalId()).isEqualTo(internalIdBefore);
        assertThat(todo.getPublicId()).isEqualTo(publicIdBefore);
        assertThat(todo.getCreatedAt()).isEqualTo(createdAtBefore);
        assertThat(todo.getTitle()).isEqualTo(titleBefore);
        assertThat(todo.getDetail()).isEqualTo(detailBefore);
        assertThat(todo.getDueDate()).isEqualTo(dueDateBefore);
        assertThat(todo.isCompleted()).isEqualTo(completedBefore);
    }
}
