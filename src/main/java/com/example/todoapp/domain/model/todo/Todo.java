package com.example.todoapp.domain.model.todo;


import java.time.LocalDateTime;

import com.example.todoapp.domain.model.todo.value.InternalId;
import com.example.todoapp.domain.model.todo.value.PublicId;

public class Todo {

    private final InternalId internalId;
    private final PublicId publicId;
    private final String title;
    private final String detail;
    private final boolean completed;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Todo(
            InternalId internalId,
            PublicId publicId,
            String title,
            String detail,
            boolean completed,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.internalId = internalId;
        this.publicId = publicId;
        this.title = title;
        this.detail = detail;
        this.completed = completed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public InternalId getInternalId() {
        return internalId;
    }

    public PublicId getPublicId() {
        return publicId;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // --- Domain の振る舞い（例） ---
    public Todo complete() {
        return new Todo(
                this.internalId,
                this.publicId,
                this.title,
                this.detail,
                true,
                this.createdAt,
                LocalDateTime.now()
        );
    }

    public Todo rename(String newTitle) {
        return new Todo(
                this.internalId,
                this.publicId,
                newTitle,
                this.detail,
                this.completed,
                this.createdAt,
                LocalDateTime.now()
        );
    }
}

