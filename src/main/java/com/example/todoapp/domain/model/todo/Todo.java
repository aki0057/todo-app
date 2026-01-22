package com.example.todoapp.domain.model.todo;


import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.todoapp.domain.model.todo.value.DueDate;
import com.example.todoapp.domain.model.todo.value.InternalId;
import com.example.todoapp.domain.model.todo.value.PublicId;
import com.example.todoapp.domain.model.todo.value.VersionNumber;

import lombok.Getter;

@Getter
public class Todo {

    private final InternalId internalId;
    private final PublicId publicId;
    private final VersionNumber versionNumber;
    private final String title;
    private final String detail;
    private final boolean completed;
    private final boolean deleted;
    private final DueDate dueDate;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Todo(
            InternalId internalId,
            PublicId publicId,
            VersionNumber versionNumber,
            String title,
            String detail,
            boolean completed,
            boolean deleted,
            DueDate dueDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.internalId = internalId;
        this.publicId = publicId;
        this.versionNumber = versionNumber;
        this.title = title;
        this.detail = detail;
        this.completed = completed;
        this.deleted = deleted;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Todo create(String title, String detail, LocalDate dueDate) {

        // バリデーション
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("タイトルは必須です");
        }

        LocalDateTime now = LocalDateTime.now();

        return new Todo(
            null,              // internalId（DB が採番）
            PublicId.generate(),          // publicId（UUID など）
            VersionNumber.initial(),      // versionNumber = 1
            title,
            detail,
            false,             // completed
            false,               // deleted
            new DueDate(dueDate),
            now,                          // createdAt
            now                           // updatedAt
        );
    }

}