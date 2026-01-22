package com.example.todoapp.domain.model.todo.value;

import java.time.LocalDate;

import jakarta.persistence.Embeddable;

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

