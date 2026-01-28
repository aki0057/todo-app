package com.example.todoapp.domain.model.todo.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("VersionNumber のテスト")
class VersionNumberTest {

    @Nested
    @DisplayName("生成 のテスト")
    class ConstructorTest {

        @Test
        @DisplayName("正常系: 正の整数で生成できる")
        void constructor_正の整数で生成できる() {
            // arrange
            int value = 1;

            // act
            VersionNumber v = new VersionNumber(value);

            // assert
            assertThat(v.value()).isEqualTo(value);
        }

        @Test
        @DisplayName("異常系: ゼロでは生成できない")
        void constructor_ゼロでは生成できない() {
            // act & assert
            assertThatThrownBy(() -> new VersionNumber(0))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: 負の値では生成できない")
        void constructor_負の値では生成できない() {
            // act & assert
            assertThatThrownBy(() -> new VersionNumber(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: nullでは生成できない")
        void constructor_nullでは生成できない() {
            // act & assert
            assertThatThrownBy(() -> new VersionNumber(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("next のテスト")
    class NextTest {

        @Test
        @DisplayName("正常系: nextメソッドで次の版数が取得できる")
        void next_次の版数が取得できる() {
            // arrange
            VersionNumber v1 = new VersionNumber(1);

            // act
            VersionNumber v2 = v1.next();

            // assert
            assertThat(v2.value()).isEqualTo(2);
        }
    }
}
