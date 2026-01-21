package com.example.todoapp.domain.model.todo.value;

import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public record PublicId(String value) {
    public PublicId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PublicIdは空にできません");
        }
        try {
            UUID.fromString(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("PublicIdはUUID形式である必要があります");
        }
    }
}
