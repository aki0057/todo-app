package com.example.todoapp.domain.model.todo.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("DueDate のテスト")
class DueDateTest {

    @Nested
    @DisplayName("生成 のテスト")
    class ConstructorTest {

        @Test
        @DisplayName("正常系: 今日以降の日付なら生成できる")
        void constructor_今日以降の日付なら生成できる() {
            // arrange
            LocalDate today = LocalDate.now();

            // act
            DueDate dueDate = new DueDate(today);

            // assert
            assertThat(dueDate.value()).isEqualTo(today);
        }

        @Test
        @DisplayName("異常系: nullなら例外が発生する")
        void constructor_nullなら例外が発生する() {
            // act & assert
            assertThatThrownBy(() -> new DueDate(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("期限日は必須です");
        }

        @Test
        @DisplayName("異常系: 過去日なら例外が発生する")
        void constructor_過去日なら例外が発生する() {
            // arrange
            LocalDate yesterday = LocalDate.now().minusDays(1);

            // act & assert
            assertThatThrownBy(() -> new DueDate(yesterday))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("期限日は本日以降である必要があります");
        }
    }
}
