package com.example.todoapp.domain.model.value;

import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public record PublicId(UUID value) {
    public PublicId {
        if (value == null) {
            throw new IllegalArgumentException("公開用IDはnullであってはなりません");
        }
    }
}
