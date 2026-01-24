package com.example.todoapp.domain.model.todo.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

class VersionNumberTest {

    @Test
    void 正の整数で生成できる() {
        // arrange
        int value = 1;

        // act
        VersionNumber v = new VersionNumber(value);

        // assert
        assertThat(v.value()).isEqualTo(value);
    }

    @Test
    void ゼロでは生成できない() {
        // act & assert
        assertThatThrownBy(() -> new VersionNumber(0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 負の値では生成できない() {
        // act & assert
        assertThatThrownBy(() -> new VersionNumber(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullでは生成できない() {
        // act & assert
        assertThatThrownBy(() -> new VersionNumber(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nextメソッドで次の版数が取得できる() {
        // arrange
        VersionNumber v1 = new VersionNumber(1);

        // act
        VersionNumber v2 = v1.next();

        // assert
        assertThat(v2.value()).isEqualTo(2);
    }
}
