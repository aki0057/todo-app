package com.example.todoapp.domain.model.todo.value;

import jakarta.persistence.Embeddable;

@Embeddable
public record PublicId(String value) {
    public PublicId {
        if (value == null) {
            throw new IllegalArgumentException("公開用IDはnullであってはなりません");
        }
    }
}
