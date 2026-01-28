package com.example.todoapp.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.todoapp.domain.exception.TodoNotFoundException;
import com.example.todoapp.domain.model.todo.Todo;
import com.example.todoapp.domain.model.todo.value.DueDate;
import com.example.todoapp.domain.model.todo.value.InternalId;
import com.example.todoapp.domain.model.todo.value.PublicId;
import com.example.todoapp.domain.model.todo.value.VersionNumber;
import com.example.todoapp.domain.repository.TodoDomainRepository;
import com.example.todoapp.infrastructure.entity.TodoHistoryEntity;
import com.example.todoapp.infrastructure.repository.jpa.TodoHistoryJpaRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("TodoService のテスト")
class TodoServiceTest {

    @Mock
    private TodoDomainRepository todoRepository;

    @Mock
    private TodoHistoryJpaRepository historyRepository;

    @InjectMocks
    private TodoService todoService;

    // テストデータ準備用のヘルパー
    private Todo createSampleTodo(Integer internalId, String publicId, int versionNumber) {
        return new Todo(
            internalId != null ? new InternalId(internalId) : null,
            new PublicId(publicId),
            new VersionNumber(versionNumber),
            "Sample Title",
            "Sample Detail",
            false,
            false,
            new DueDate(LocalDate.now().plusDays(1)),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    // 正しいUUID形式の定数
    private static final String VALID_UUID_1 = "550e8400-e29b-41d4-a716-446655440000";
    private static final String VALID_UUID_2 = "550e8400-e29b-41d4-a716-446655440001";
    private static final String VALID_UUID_3 = "550e8400-e29b-41d4-a716-446655440002";
    private static final String NON_EXISTENT_UUID = "00000000-0000-0000-0000-000000000000";

    // ========================================================================
    // createTodo テスト
    // ========================================================================
    @Nested
    @DisplayName("createTodo のテスト")
    class CreateTodoTest {

        @Test
        @DisplayName("正常系: Todoを作成し履歴も保存される")
        void createTodo_正常系() {
            // arrange
            String expectedTitle = "Test Title";
            String expectedDetail = "Test Detail";
            LocalDate expectedDueDate = LocalDate.now().plusDays(1);

            Todo savedTodo = new Todo(
                new InternalId(1),
                new PublicId(VALID_UUID_1),
                new VersionNumber(1),
                expectedTitle,
                expectedDetail,
                false,
                false,
                new DueDate(expectedDueDate),
                LocalDateTime.now(),
                LocalDateTime.now()
            );

            ArgumentCaptor<Todo> todoCaptor = ArgumentCaptor.forClass(Todo.class);

            when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

            // act
            Todo result = todoService.createTodo(expectedTitle, expectedDetail, expectedDueDate);

            // assert
            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo(expectedTitle);
            assertThat(result.getDetail()).isEqualTo(expectedDetail);
            assertThat(result.getDueDate().value()).isEqualTo(expectedDueDate);
            
            verify(todoRepository, times(1)).save(todoCaptor.capture());
            Todo captured = todoCaptor.getValue();
            assertThat(captured.getTitle()).isEqualTo(expectedTitle);
            assertThat(captured.getDetail()).isEqualTo(expectedDetail);
            assertThat(captured.getDueDate().value()).isEqualTo(expectedDueDate);
            verify(historyRepository, times(1)).save(any(TodoHistoryEntity.class));
        }

        @Test
        @DisplayName("異常系: タイトルがnullの場合は例外")
        void createTodo_タイトルがnull() {
            // arrange
            LocalDate dueDate = LocalDate.now().plusDays(1);

            // act & assert
            assertThatThrownBy(() -> todoService.createTodo(null, "Detail", dueDate))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("タイトルは必須です");

            verify(todoRepository, never()).save(any(Todo.class));
            verify(historyRepository, never()).save(any(TodoHistoryEntity.class));
        }

        @Test
        @DisplayName("異常系: タイトルが空白の場合は例外")
        void createTodo_タイトルが空白() {
            // arrange
            LocalDate dueDate = LocalDate.now().plusDays(1);

            // act & assert
            assertThatThrownBy(() -> todoService.createTodo("   ", "Detail", dueDate))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("タイトルは必須です");

            verify(todoRepository, never()).save(any(Todo.class));
        }
    }

    // ================================================================
    // updateTodo
    // ================================================================
    @Nested
    @DisplayName("updateTodo のテスト")
    class UpdateTodoTest {

        @Test
        @DisplayName("正常系: Todoを更新し履歴も保存される")
        void updateTodo_正常系() {
            // arrange
            String publicId = VALID_UUID_1;
            Todo existingTodo = createSampleTodo(1, publicId, 1); 

            String updatedTitle = "Updated Title";
            String updatedDetail = "Updated Detail";
            LocalDate updatedDueDate = LocalDate.now().plusDays(2);

            ArgumentCaptor<Todo> todoCaptor = ArgumentCaptor.forClass(Todo.class);

            when(todoRepository.findByPublicId(any(PublicId.class)))
                .thenReturn(Optional.of(existingTodo));
            when(todoRepository.save(any(Todo.class)))
                .thenAnswer(inv -> inv.getArgument(0));
            
            // act
            Todo result = todoService.updateTodo(publicId, updatedTitle, updatedDetail, updatedDueDate);

            // assert
            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo(updatedTitle);
            assertThat(result.getDetail()).isEqualTo(updatedDetail);
            assertThat(result.getDueDate().value()).isEqualTo(updatedDueDate);
            assertThat(result.getVersionNumber().value()).isEqualTo(2);
            verify(todoRepository, times(1)).findByPublicId(any(PublicId.class));
            verify(todoRepository, times(1)).save(todoCaptor.capture());

            Todo captured = todoCaptor.getValue();
            assertThat(captured.getTitle()).isEqualTo(updatedTitle);
            assertThat(captured.getDetail()).isEqualTo(updatedDetail);
            assertThat(captured.getDueDate().value()).isEqualTo(updatedDueDate);
            assertThat(captured.getVersionNumber().value()).isEqualTo(2);
            verify(historyRepository, times(1)).save(any(TodoHistoryEntity.class));
        }

        @Test
        @DisplayName("異常系: 存在しないTodoを更新しようとすると例外")
        void updateTodo_存在しないTodo() {
            // arrange
            String publicId = NON_EXISTENT_UUID;
            when(todoRepository.findByPublicId(any(PublicId.class)))
                    .thenReturn(Optional.empty());

            // act & assert
            assertThatThrownBy(() -> todoService.updateTodo(publicId, "Title", "Detail", LocalDate.now().plusDays(1)))
                    .isInstanceOf(TodoNotFoundException.class)
                    .hasMessageContaining("Todoが見つかりません");

            verify(todoRepository, times(1)).findByPublicId(any(PublicId.class));
            verify(todoRepository, never()).save(any(Todo.class));
            verify(historyRepository, never()).save(any(TodoHistoryEntity.class));
        }

        @Test
        @DisplayName("異常系: 更新時にタイトルが空白の場合は例外")
        void updateTodo_タイトルが空白() {
            // arrange
            String publicId = VALID_UUID_1;
            Todo existingTodo = createSampleTodo(1, publicId, 1);
            
            when(todoRepository.findByPublicId(any(PublicId.class)))
                    .thenReturn(Optional.of(existingTodo));

            // act & assert
            assertThatThrownBy(() -> todoService.updateTodo(publicId, "", "Detail", LocalDate.now().plusDays(1)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("タイトルは必須です");

            verify(todoRepository, never()).save(any(Todo.class));
        }
    }

    // ========================================================================
    // completeTodo テスト
    // ========================================================================
    @Nested
    @DisplayName("completeTodo のテスト")
    class CompleteTodoTest {

        @Test
        @DisplayName("正常系: Todoを完了状態にし履歴も保存される")
        void completeTodo_正常系() {
            // arrange
            String publicId = VALID_UUID_1;
            Todo existingTodo = createSampleTodo(1, publicId, 1); 

            ArgumentCaptor<Todo> todoCaptor = ArgumentCaptor.forClass(Todo.class);

            when(todoRepository.findByPublicId(any(PublicId.class)))
                .thenReturn(Optional.of(existingTodo));
            when(todoRepository.save(any(Todo.class)))
                .thenAnswer(inv -> inv.getArgument(0));
            
            // act
            Todo result = todoService.completeTodo(publicId);

            // assert
            assertThat(result).isNotNull();
            assertThat(result.isCompleted()).isTrue();
            assertThat(result.getTitle()).isEqualTo(existingTodo.getTitle());
            assertThat(result.getDetail()).isEqualTo(existingTodo.getDetail());
            assertThat(result.getDueDate().value()).isEqualTo(existingTodo.getDueDate().value());
            assertThat(result.getVersionNumber().value()).isEqualTo(2);
            assertThat(result.isDeleted()).isFalse();
            verify(todoRepository, times(1)).findByPublicId(any(PublicId.class));
            verify(todoRepository, times(1)).save(todoCaptor.capture());

            Todo captured = todoCaptor.getValue();
            assertThat(captured.isCompleted()).isTrue();
            assertThat(captured.getTitle()).isEqualTo(existingTodo.getTitle());
            assertThat(captured.getDetail()).isEqualTo(existingTodo.getDetail());
            assertThat(captured.getDueDate().value()).isEqualTo(existingTodo.getDueDate().value());
            assertThat(captured.getVersionNumber().value()).isEqualTo(2);
            assertThat(captured.isDeleted()).isFalse();
            verify(historyRepository, times(1)).save(any(TodoHistoryEntity.class));
        }

        @Test
        @DisplayName("異常系: 存在しないTodoを完了しようとすると例外")
        void completeTodo_存在しないTodo() {
            // arrange
            String publicId = NON_EXISTENT_UUID;
            when(todoRepository.findByPublicId(any(PublicId.class)))
                    .thenReturn(Optional.empty());

            // act & assert
            assertThatThrownBy(() -> todoService.completeTodo(publicId))
                    .isInstanceOf(TodoNotFoundException.class)
                    .hasMessageContaining("Todoが見つかりません");

            verify(todoRepository, times(1)).findByPublicId(any(PublicId.class));
            verify(todoRepository, never()).save(any(Todo.class));
        }

        @Test
        @DisplayName("重複完了: すでに完了状態のTodoを完了するとスキップされる")
        void completeTodo_重複完了() {
            // arrange
            String publicId = VALID_UUID_1;
            Todo completedTodo = new Todo(
                new InternalId(1),
                new PublicId(publicId),
                new VersionNumber(2),
                "Sample Title",
                "Sample Detail",
                true,
                false,
                new DueDate(LocalDate.now().plusDays(1)),
                LocalDateTime.now(),
                LocalDateTime.now()
            );

            when(todoRepository.findByPublicId(any(PublicId.class)))
                .thenReturn(Optional.of(completedTodo));

            // act
            Todo result = todoService.completeTodo(publicId);

            // assert
            assertThat(result).isNotNull();
            assertThat(result.isCompleted()).isTrue();
            verify(todoRepository, times(1)).findByPublicId(any(PublicId.class));
            verify(todoRepository, never()).save(any(Todo.class));
            verify(historyRepository, never()).save(any(TodoHistoryEntity.class));
        }
    }

    // ========================================================================
    // deleteTodo テスト
    // ========================================================================
    @Nested
    @DisplayName("deleteTodo のテスト")
    class DeleteTodoTest {

        @Test
        @DisplayName("正常系: Todoを論理削除し履歴も保存される")
        void deleteTodo_正常系() {
            // arrange
            String publicId = VALID_UUID_1;
            Todo existingTodo = createSampleTodo(1, publicId, 1); 

            ArgumentCaptor<Todo> todoCaptor = ArgumentCaptor.forClass(Todo.class);

            when(todoRepository.findByPublicId(any(PublicId.class)))
                .thenReturn(Optional.of(existingTodo));
            when(todoRepository.save(any(Todo.class)))
                .thenAnswer(inv -> inv.getArgument(0));
            
            // act
            Todo result = todoService.deleteTodo(publicId);

            // assert
            assertThat(result).isNotNull();
            assertThat(result.isDeleted()).isTrue();
            assertThat(result.getTitle()).isEqualTo(existingTodo.getTitle());
            assertThat(result.getDetail()).isEqualTo(existingTodo.getDetail());
            assertThat(result.getDueDate().value()).isEqualTo(existingTodo.getDueDate().value());
            assertThat(result.getVersionNumber().value()).isEqualTo(2);
            assertThat(result.isCompleted()).isFalse();
            verify(todoRepository, times(1)).findByPublicId(any(PublicId.class));
            verify(todoRepository, times(1)).save(todoCaptor.capture());

            Todo captured = todoCaptor.getValue();
            assertThat(captured.isDeleted()).isTrue();
            assertThat(captured.getTitle()).isEqualTo(existingTodo.getTitle());
            assertThat(captured.getDetail()).isEqualTo(existingTodo.getDetail());
            assertThat(captured.getDueDate().value()).isEqualTo(existingTodo.getDueDate().value());
            assertThat(captured.getVersionNumber().value()).isEqualTo(2);
            assertThat(captured.isCompleted()).isFalse();
            verify(historyRepository, times(1)).save(any(TodoHistoryEntity.class));
        }

        @Test
        @DisplayName("異常系: 存在しないTodoを削除しようとすると例外")
        void deleteTodo_存在しないTodo() {
            // arrange
            String publicId = NON_EXISTENT_UUID;
            when(todoRepository.findByPublicId(any(PublicId.class)))
                    .thenReturn(Optional.empty());

            // act & assert
            assertThatThrownBy(() -> todoService.deleteTodo(publicId))
                    .isInstanceOf(TodoNotFoundException.class)
                    .hasMessageContaining("Todoが見つかりません");

            verify(todoRepository, times(1)).findByPublicId(any(PublicId.class));
            verify(todoRepository, never()).save(any(Todo.class));
        }
    }

    // ================================================================
    // getTodo
    // ================================================================
    @Nested
    @DisplayName("getTodo のテスト")
    class GetTodoTest {

        @Test
        @DisplayName("正常系: 指定したPublicIDのTodoを取得できる")
        void getTodo_正常系() {
            // arrange
            String publicId = VALID_UUID_1;
            Todo existingTodo = createSampleTodo(1, publicId, 1);
            
            when(todoRepository.findByPublicId(any(PublicId.class)))
                    .thenReturn(Optional.of(existingTodo));

            // act
            Todo result = todoService.getTodo(publicId);

            // assert
            assertThat(result).isNotNull();
            assertThat(result.getPublicId().value()).isEqualTo(publicId);
            verify(todoRepository, times(1)).findByPublicId(any(PublicId.class));
        }

        @Test
        @DisplayName("異常系: 存在しないTodoを取得しようとすると例外")
        void getTodo_存在しないTodo() {
            // arrange
            String publicId = NON_EXISTENT_UUID;
            when(todoRepository.findByPublicId(any(PublicId.class)))
                    .thenReturn(Optional.empty());

            // act & assert
            assertThatThrownBy(() -> todoService.getTodo(publicId))
                    .isInstanceOf(TodoNotFoundException.class)
                    .hasMessageContaining("Todoが見つかりません");

            verify(todoRepository, times(1)).findByPublicId(any(PublicId.class));
        }

        @Test
        @DisplayName("異常系: 削除済みTodoを取得しようとすると例外")
        void getTodo_削除済みTodo() {
            // arrange
            String publicId = VALID_UUID_1;
            Todo deletedTodo = createSampleTodo(1, publicId, 1);
            deletedTodo.delete();
            
            when(todoRepository.findByPublicId(any(PublicId.class)))
                    .thenReturn(Optional.of(deletedTodo));

            // act & assert
            assertThatThrownBy(() -> todoService.getTodo(publicId))
                    .isInstanceOf(TodoNotFoundException.class)
                    .hasMessageContaining("Todoが見つかりません");

            verify(todoRepository, times(1)).findByPublicId(any(PublicId.class));
        }
    }

    // ========================================================================
    // listActiveTodos テスト
    // ========================================================================
    @Nested
    @DisplayName("listActiveTodos のテスト")
    class ListActiveTodosTest {

        @Test
        @DisplayName("正常系: 削除済みを除外したTodo一覧を取得できる")
        void listActiveTodos_削除済みを除外() {
            // arrange
            Todo activeTodo1 = createSampleTodo(1, VALID_UUID_1, 1);
            Todo activeTodo2 = createSampleTodo(2, VALID_UUID_2, 1);
            Todo deletedTodo = createSampleTodo(3, VALID_UUID_3, 2);
            deletedTodo.delete();

            when(todoRepository.findAll())
                    .thenReturn(List.of(activeTodo1, activeTodo2, deletedTodo));

            // act
            List<Todo> result = todoService.listActiveTodos();

            // assert
            assertThat(result).hasSize(2);
            assertThat(result).extracting(t -> t.getPublicId().value())
                    .containsExactlyInAnyOrder(VALID_UUID_1, VALID_UUID_2);
            verify(todoRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("正常系: 全て削除済みの場合は空リストを返す")
        void listActiveTodos_全て削除済み() {
            // arrange
            Todo deletedTodo1 = createSampleTodo(1, VALID_UUID_1, 2);
            deletedTodo1.delete();
            Todo deletedTodo2 = createSampleTodo(2, VALID_UUID_2, 2);
            deletedTodo2.delete();

            when(todoRepository.findAll())
                    .thenReturn(List.of(deletedTodo1, deletedTodo2));

            // act
            List<Todo> result = todoService.listActiveTodos();

            // assert
            assertThat(result).isEmpty();
            verify(todoRepository, times(1)).findAll();
        }
    }
}
