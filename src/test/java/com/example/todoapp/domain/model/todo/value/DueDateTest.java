package com.example.todoapp.domain.model.todo.value;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

class DueDateTest {

    @Test
    void 今日以降の日付なら生成できる() {
        // arrange
        LocalDate today = LocalDate.now();

        // act
        DueDate dueDate = new DueDate(today);

        // assert
        assertThat(dueDate.value()).isEqualTo(today);
    }

    @Test
    void nullなら例外が発生する() {
        // act & assert
        assertThatThrownBy(() -> new DueDate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("期限日は必須です");
    }

    @Test
    void 過去日なら例外が発生する() {
        // arrange
        LocalDate yesterday = LocalDate.now().minusDays(1);

        // act & assert
        assertThatThrownBy(() -> new DueDate(yesterday))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("期限日は本日以降である必要があります");
    }
}
