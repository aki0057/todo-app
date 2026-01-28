package com.example.todoapp.domain.model.todo.value;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;

/**
 * Todo の期限日を表す ValueObject。
 *
 * <p>本日以降の日付のみ有効。過去日付は許可されない。
 */
@Embeddable
public record DueDate(LocalDate value) {

    public DueDate {
        if (value == null) {
            throw new IllegalArgumentException("期限日は必須です");
        }
        if (value.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("期限日は本日以降である必要があります");
        }
    }
}
