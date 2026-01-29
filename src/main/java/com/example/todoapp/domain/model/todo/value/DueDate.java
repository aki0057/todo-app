package com.example.todoapp.domain.model.todo.value;

import java.time.LocalDate;

import jakarta.persistence.Embeddable;

/**
 * Todo の期限日を表す ValueObject。
 *
 * <p>日付の構造的な妥当性のみを保証する。
 * 期限日が本日以降であるかの検証は、作成・更新時にエンティティ側で行う。
 */
@Embeddable
public record DueDate(LocalDate value) {

    public DueDate {
        if (value == null) {
            throw new IllegalArgumentException("期限日は必須です");
        }
    }
}
