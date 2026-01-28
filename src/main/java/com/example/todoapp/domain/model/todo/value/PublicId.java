package com.example.todoapp.domain.model.todo.value;

import java.util.UUID;

import jakarta.persistence.Embeddable;

/**
 * Todo の公開IDを表す ValueObject。
 * <p>
 * UUID 形式の文字列で、外部に公開される識別子。
 * ドメイン外部のシステムとの連携に使用される。
 * </p>
 */
@Embeddable
public record PublicId(String value) {
    public PublicId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("公開IDは空にできません");
        }
        try {
            UUID.fromString(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("公開IDはUUID形式である必要があります");
        }
    }

    /**
     * 新しい公開IDを生成する。
     * <p>
     * 新規 Todo 作成時に自動生成される UUID 形式の ID。
     * </p>
     *
     * @return 生成された公開ID
     */
    public static PublicId generate() {
        return new PublicId(UUID.randomUUID().toString());
    }
}
