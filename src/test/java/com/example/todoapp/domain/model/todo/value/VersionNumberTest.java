package com.example.todoapp.domain.model.todo.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

class VersionNumberTest {

    @Test
    void 正の整数で生成できる() {
        VersionNumber v = new VersionNumber(1);
        assertThat(v.value()).isEqualTo(1);
    }

    @Test
    void ゼロでは生成できない() {
        assertThatThrownBy(() -> new VersionNumber(0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 負の値では生成できない() {
        assertThatThrownBy(() -> new VersionNumber(-1))
            .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test
    void nullでは生成できない() {
        assertThatThrownBy(() -> new VersionNumber(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nextメソッドで次の版数が取得できる() {
        VersionNumber v1 = new VersionNumber(1);
        VersionNumber v2 = v1.next();

        assertThat(v2.value()).isEqualTo(2);
    }
    
}