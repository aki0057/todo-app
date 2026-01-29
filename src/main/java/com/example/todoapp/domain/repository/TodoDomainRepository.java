package com.example.todoapp.domain.repository;

import java.util.List;
import java.util.Optional;

import com.example.todoapp.domain.model.todo.Todo;
import com.example.todoapp.domain.model.todo.value.InternalId;
import com.example.todoapp.domain.model.todo.value.PublicId;

/**
 * Todo の永続化を行うドメイン repository インターフェース。
 *
 * <p>ドメイン層が依存する repository の仕様を定義し、 具体的な永続化実装（JPA など）はインフラストラクチャ層に任せる。 DDD
 * のリポジトリパターンに従い、ドメインモデルの集約を管理する。
 */
public interface TodoDomainRepository {

    /**
     * 内部IDで Todo を検索する。
     *
     * @param id 検索対象の内部ID
     * @return 見つかった場合は Todo を含む Optional、見つからない場合は空の Optional
     */
    Optional<Todo> findByInternalId(InternalId id);

    /**
     * 公開IDで Todo を検索する。
     *
     * @param id 検索対象の公開ID
     * @return 見つかった場合は Todo を含む Optional、見つからない場合は空の Optional
     */
    Optional<Todo> findByPublicId(PublicId id);

    /**
     * すべての Todo を取得する。
     *
     * @return Todo のリスト
     */
    List<Todo> findAll();

    /**
     * 削除されておらず、期限日が本日以降のTodoを取得する。
     *
     * <p>期限日の昇順、その後作成日時の昇順でソートされる。
     *
     * @return 有効なTodoのリスト（ソート済み）
     */
    List<Todo> findAllActiveAndValid();

    /**
     * Todo を永続化する。
     *
     * @param todo 永続化対象の Todo
     * @return 永続化後の Todo
     */
    Todo save(Todo todo);
}
