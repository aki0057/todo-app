package com.example.todoapp.infrastructure.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.todoapp.domain.model.todo.Todo;
import com.example.todoapp.domain.model.todo.value.InternalId;
import com.example.todoapp.domain.model.todo.value.PublicId;
import com.example.todoapp.domain.repository.TodoDomainRepository;
import com.example.todoapp.infrastructure.entity.TodoEntity;
import com.example.todoapp.infrastructure.mapper.TodoMapper;
import com.example.todoapp.infrastructure.repository.jpa.TodoJpaRepository;

/**
 * ドメイン repository インターフェースの実装クラス。
 *
 * <p>Spring Data JPA の TodoJpaRepository をアダプターとして使用し、 ドメイン層に対して repository パターンを提供する。 Entity と
 * Domain の変換は TodoMapper で行う。
 */
@Repository
public class TodoRepositoryImpl implements TodoDomainRepository {

    private final TodoJpaRepository jpa;

    /**
     * TodoRepositoryImpl を構築する。
     *
     * @param jpa Spring Data JPA の TodoJpaRepository
     */
    public TodoRepositoryImpl(TodoJpaRepository jpa) {
        this.jpa = jpa;
    }

    /**
     * 内部IDで Todo を検索する。
     *
     * @param id 検索対象の内部ID
     * @return 見つかった場合は Todo を含む Optional、見つからない場合は空の Optional
     */
    @Override
    public Optional<Todo> findByInternalId(InternalId id) {
        return jpa.findByInternalId(id.value()).map(TodoMapper::toDomain);
    }

    /**
     * 公開IDで Todo を検索する。
     *
     * @param id 検索対象の公開ID
     * @return 見つかった場合は Todo を含む Optional、見つからない場合は空の Optional
     */
    @Override
    public Optional<Todo> findByPublicId(PublicId id) {
        return jpa.findByPublicId(id.value()).map(TodoMapper::toDomain);
    }

    /**
     * すべての Todo を取得する。
     *
     * @return Todo のリスト
     */
    @Override
    public List<Todo> findAll() {
        return jpa.findAll().stream().map(TodoMapper::toDomain).toList();
    }

    /**
     * 削除されておらず、期限日が本日以降のTodoを取得する。
     *
     * <p>期限日の昇順、その後作成日時の昇順でソートされる。
     *
     * @return 有効なTodoのリスト（ソート済み）
     */
    @Override
    public List<Todo> findAllActiveAndValid() {
        return jpa.findAllActiveAndValid().stream().map(TodoMapper::toDomain).toList();
    }

    /**
     * Todo を永続化する。
     *
     * <p>新規作成の場合は INSERT、既存の場合は UPDATE が実行される。
     *
     * @param todo 永続化対象の Todo
     * @return 永続化後の Todo（内部IDが設定済み）
     */
    @Override
    public Todo save(Todo todo) {
        TodoEntity entity = TodoMapper.toEntity(todo);
        TodoEntity saved = jpa.save(entity);
        return TodoMapper.toDomain(saved);
    }
}
