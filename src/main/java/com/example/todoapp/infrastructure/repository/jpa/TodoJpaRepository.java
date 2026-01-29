package com.example.todoapp.infrastructure.repository.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.todoapp.infrastructure.entity.TodoEntity;

@Repository
public interface TodoJpaRepository extends JpaRepository<TodoEntity, Integer> {
    Optional<TodoEntity> findByInternalId(Integer internalId);

    Optional<TodoEntity> findByPublicId(String publicId);

    /**
     * 削除されておらず、期限日が本日以降のTodoを取得する。
     *
     * <p>期限日の昇順、その後作成日時の昇順でソートされる。
     *
     * @return 有効なTodoのリスト（ソート済み）
     */
    @Query(
            "SELECT t FROM TodoEntity t WHERE t.deletedFlag = false "
                    + "AND t.dueDate >= CURRENT_DATE "
                    + "ORDER BY t.dueDate ASC, t.createdAt ASC")
    List<TodoEntity> findAllActiveAndValid();
}
