package com.example.todoapp.domain.model.todo.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("InternalId のテスト")
class InternalIdTest {

    @Nested
    @DisplayName("生成 のテスト")
    class ConstructorTest {

        @Test
        @DisplayName("正常系: 正の整数で生成できる")
        void constructor_正の整数で生成できる() {
            // arrange
            int value = 1;

            // act
            InternalId id = new InternalId(value);

            // assert
            assertThat(id.value()).isEqualTo(value);
        }

        @Test
        @DisplayName("異常系: ゼロでは生成できない")
        void constructor_ゼロでは生成できない() {
            // act & assert
            assertThatThrownBy(() -> new InternalId(0))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: 負の値では生成できない")
        void constructor_負の値では生成できない() {
            // act & assert
            assertThatThrownBy(() -> new InternalId(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
