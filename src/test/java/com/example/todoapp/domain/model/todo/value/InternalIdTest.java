package com.example.todoapp.domain.model.todo.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

class InternalIdTest {

    @Test
    void 正の整数で生成できる() {
        InternalId id = new InternalId(1);

        assertThat(id.value()).isEqualTo(1);
    }

    @Test
    void ゼロでは生成できない() {
        assertThatThrownBy(() -> new InternalId(0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 負の値では生成できない() {
        assertThatThrownBy(() -> new InternalId(-1))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
}
