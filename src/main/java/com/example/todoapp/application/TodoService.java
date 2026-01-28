package com.example.todoapp.application;

import com.example.todoapp.domain.exception.TodoNotFoundException;
import com.example.todoapp.domain.model.todo.Todo;
import com.example.todoapp.domain.model.todo.value.PublicId;
import com.example.todoapp.domain.repository.TodoDomainRepository;
import com.example.todoapp.infrastructure.entity.TodoHistoryEntity;
import com.example.todoapp.infrastructure.repository.jpa.TodoHistoryJpaRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Todo のユースケースを実装するアプリケーションサービス。
 *
 * <p>ドメインレイヤーの Todo とインフラストラクチャ層の TodoEntity を調整し、 ビジネスロジックを実行する。履歴管理も併せて実施する。
 */
@Service
public class TodoService {

    private final TodoDomainRepository todoRepository;
    private final TodoHistoryJpaRepository historyRepository;

    /**
     * TodoService を構築する。
     *
     * @param todoRepository Todo の永続化リポジトリ
     * @param historyRepository Todo 履歴の永続化リポジトリ
     */
    public TodoService(
            TodoDomainRepository todoRepository, TodoHistoryJpaRepository historyRepository) {
        this.todoRepository = todoRepository;
        this.historyRepository = historyRepository;
    }

    // ========================================================================
    // Create
    // ========================================================================
    /**
     * 新しい Todo を作成する。
     *
     * <p>ドメインの Todo.create() でビジネスロジックを検証し、 リポジトリに永続化する。その後、履歴を記録する。
     *
     * @param title タイトル（必須、100文字以内）
     * @param detail 詳細（任意、1000文字以内）
     * @param dueDate 期限日（必須、本日以降）
     * @return 新しく作成された Todo
     * @throws IllegalArgumentException 入力値が不正な場合
     */
    @Transactional
    public Todo createTodo(String title, String detail, LocalDate dueDate) {
        Todo newTodo = Todo.create(title, detail, dueDate);
        Todo saved = todoRepository.save(newTodo);
        // 履歴を保存
        saveHistory(saved);
        return saved;
    }

    // ========================================================================
    // Update
    // ========================================================================
    /**
     * 既存の Todo を更新する。
     *
     * <p>公開IDで対象 Todo を検索し、内容を更新する。 版数が変わった場合の み履歴を記録する。
     *
     * @param publicId 公開ID（UUID形式）
     * @param title 更新後のタイトル（必須、100文字以内）
     * @param detail 更新後の詳細（任意、1000文字以内）
     * @param dueDate 更新後の期限日（必須、本日以降）
     * @return 更新後の Todo
     * @throws TodoNotFoundException Todo が見つからない場合
     * @throws IllegalArgumentException 入力値が不正な場合
     */
    @Transactional
    public Todo updateTodo(String publicId, String title, String detail, LocalDate dueDate) {
        Todo todo =
                todoRepository
                        .findByPublicId(new PublicId(publicId))
                        .orElseThrow(() -> new TodoNotFoundException());

        int beforeVersion = todo.getVersionNumber().value();
        todo.update(title, detail, dueDate);
        Todo saved = todoRepository.save(todo);

        // 版数が増えた場合のみ履歴保存
        if (saved.getVersionNumber().value() > beforeVersion) {
            saveHistory(saved);
        }
        return saved;
    }

    // ========================================================================
    // Complete
    // ========================================================================
    /**
     * 指定された Todo を完了状態にする。
     *
     * <p>公開IDで対象 Todo を検索し、完了処理を実行する。 すでに完了状態の場合はスキップする。
     *
     * @param publicId 公開ID（UUID形式）
     * @return 完了後の Todo
     * @throws TodoNotFoundException Todo が見つからない場合
     */
    @Transactional
    public Todo completeTodo(String publicId) {
        Todo todo =
                todoRepository
                        .findByPublicId(new PublicId(publicId))
                        .orElseThrow(() -> new TodoNotFoundException());

        // すでに完了状態の場合はスキップ
        if (todo.isCompleted()) {
            return todo;
        }

        int beforeVersion = todo.getVersionNumber().value();
        todo.complete();
        Todo saved = todoRepository.save(todo);

        if (saved.getVersionNumber().value() > beforeVersion) {
            saveHistory(saved);
        }
        return saved;
    }

    // ========================================================================
    // Delete (Logical)
    // ========================================================================
    /**
     * 指定された Todo を論理削除する。
     *
     * <p>公開IDで対象 Todo を検索し、削除フラグを立てる。 物理削除ではなく論理削除（ソフトデリート）を行う。
     *
     * @param publicId 公開ID（UUID形式）
     * @return 削除後の Todo
     * @throws TodoNotFoundException Todo が見つからない場合
     */
    @Transactional
    public Todo deleteTodo(String publicId) {
        Todo todo =
                todoRepository
                        .findByPublicId(new PublicId(publicId))
                        .orElseThrow(() -> new TodoNotFoundException());

        int beforeVersion = todo.getVersionNumber().value();
        todo.delete();
        Todo saved = todoRepository.save(todo);

        if (saved.getVersionNumber().value() > beforeVersion) {
            saveHistory(saved);
        }
        return saved;
    }

    // ========================================================================
    // Query
    // ========================================================================
    /**
     * 公開IDで指定された Todo を取得する。
     *
     * <p>論理削除済みの Todo は存在しないものとして扱い、例外をスローする。
     *
     * @param publicId 公開ID（UUID形式）
     * @return 指定された Todo
     * @throws TodoNotFoundException Todo が見つからない、または削除済みの場合
     */
    @Transactional(readOnly = true)
    public Todo getTodo(String publicId) {
        Todo todo =
                todoRepository
                        .findByPublicId(new PublicId(publicId))
                        .orElseThrow(() -> new TodoNotFoundException());

        // 削除済みTodoは存在しないものとして扱う
        if (todo.isDeleted()) {
            throw new TodoNotFoundException();
        }

        return todo;
    }

    /**
     * 論理削除されていない活動中の Todo をすべて取得する。
     *
     * <p>期限日の昇順、その後作成日時の昇順でソートされた状態で返される。
     *
     * @return 活動中の Todo のリスト（ソート済み）
     */
    @Transactional(readOnly = true)
    public List<Todo> listActiveTodos() {
        return todoRepository.findAll().stream()
                .filter(t -> !t.isDeleted())
                .sorted(
                        Comparator.comparing((Todo todo) -> todo.getDueDate().value())
                                .thenComparing(Todo::getCreatedAt))
                .collect(Collectors.toList());
    }

    // ========================================================================
    // Helper
    // ========================================================================
    /**
     * Todo の変更履歴を保存する。
     *
     * <p>内部IDが設定されていない場合（JPA採番前）は保存をスキップする。
     *
     * @param todo 履歴を記録する Todo
     */
    private void saveHistory(Todo todo) {
        if (todo.getInternalId() == null) {
            // 念のため internalId がない場合は保存しない（JPA 採番前）
            return;
        }
        TodoHistoryEntity history = new TodoHistoryEntity();
        history.setInternalId(todo.getInternalId().value());
        history.setVersionNumber(todo.getVersionNumber().value());
        history.setPublicId(todo.getPublicId().value());
        history.setTitle(todo.getTitle());
        history.setDetail(todo.getDetail());
        history.setDueDate(todo.getDueDate().value());
        history.setCompletedFlag(todo.isCompleted());
        history.setDeletedFlag(todo.isDeleted());
        // createdAt / updatedAt は @CreationTimestamp / @UpdateTimestamp で管理

        historyRepository.save(history);
    }
}
