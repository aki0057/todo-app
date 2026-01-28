package com.example.todoapp.domain.model.todo.value;

import jakarta.persistence.Embeddable;

/**
 * Todo の内部IDを表す ValueObject。
 * <p>
 * データベースが採番する自動採番 ID。1以上の正の整数のみ有効。
 * </p>
 */
@Embeddable
public record InternalId(Integer value) {
    public InternalId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("内部 ID は1以上の整数でなければなりません");
        }
    }
}

