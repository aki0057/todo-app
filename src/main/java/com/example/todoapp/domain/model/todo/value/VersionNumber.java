package com.example.todoapp.domain.model.todo.value;

import jakarta.persistence.Embeddable;

@Embeddable
public record VersionNumber(Integer value) {

    private static final int INITIAL = 1;

    public VersionNumber {
        if (value == null || value < 1) {
            throw new IllegalArgumentException("版数は1以上の整数でなければなりません");
        }
    }

    public static VersionNumber initial() {
        return new VersionNumber(INITIAL);
    }

    public VersionNumber next() {
        return new VersionNumber(value + 1);
    }
}
