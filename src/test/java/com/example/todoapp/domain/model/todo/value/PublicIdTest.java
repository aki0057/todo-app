package com.example.todoapp.domain.model.todo.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

class PublicIdTest {

    @Test
    void 正しいUUID文字列なら生成できる() {
        String uuid = "123e4567-e89b-12d3-a456-426614174000";
        PublicId id = new PublicId(uuid);

        assertThat(id.value()).isEqualTo(uuid);
    }

    @Test
    void 空文字では生成できない() {
        assertThatThrownBy(() -> new PublicId(""))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 不正なUUID文字列では生成できない() {
        assertThatThrownBy(() -> new PublicId("not-a-uuid"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
