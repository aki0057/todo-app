package com.example.todoapp.domain.model.value;

import jakarta.persistence.Embeddable;

@Embeddable
public record InternalId(Integer value) {
    public InternalId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("内部用IDは1以上の整数でなければなりません");
        }
    }
}

