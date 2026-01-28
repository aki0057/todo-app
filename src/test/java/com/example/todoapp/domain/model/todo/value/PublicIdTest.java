package com.example.todoapp.domain.model.todo.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("PublicId のテスト")
class PublicIdTest {

    @Nested
    @DisplayName("生成 のテスト")
    class ConstructorTest {

        @Test
        @DisplayName("正常系: 正しいUUID文字列なら生成できる")
        void constructor_正しいUUID文字列なら生成できる() {
            // arrange
            String uuid = "123e4567-e89b-12d3-a456-426614174000";

            // act
            PublicId id = new PublicId(uuid);

            // assert
            assertThat(id.value()).isEqualTo(uuid);
        }

        @Test
        @DisplayName("異常系: 空文字では生成できない")
        void constructor_空文字では生成できない() {
            // act & assert
            assertThatThrownBy(() -> new PublicId("")).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("異常系: 不正なUUID文字列では生成できない")
        void constructor_不正なUUID文字列では生成できない() {
            // act & assert
            assertThatThrownBy(() -> new PublicId("not-a-uuid"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
