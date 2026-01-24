package com.example.todoapp.domain.model.todo.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

class InternalIdTest {

    @Test
    void 正の整数で生成できる() {
        // arrange
        int value = 1;

        // act
        InternalId id = new InternalId(value);

        // assert
        assertThat(id.value()).isEqualTo(value);
    }

    @Test
    void ゼロでは生成できない() {
        // act & assert
        assertThatThrownBy(() -> new InternalId(0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 負の値では生成できない() {
        // act & assert
        assertThatThrownBy(() -> new InternalId(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
