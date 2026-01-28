package com.example.todoapp.infrastructure.mapper;

import com.example.todoapp.domain.model.todo.Todo;
import com.example.todoapp.domain.model.todo.value.DueDate;
import com.example.todoapp.domain.model.todo.value.InternalId;
import com.example.todoapp.domain.model.todo.value.PublicId;
import com.example.todoapp.domain.model.todo.value.VersionNumber;
import com.example.todoapp.infrastructure.entity.TodoEntity;

/**
 * ドメイン層の Todo とインフラストラクチャ層の TodoEntity の相互変換を行うマッパー。
 *
 * <p>Entity から Domain への変換時は ValueObject のラッピングを実施し、 Domain から Entity への変換時は ValueObject から値を抽出する。
 */
public class TodoMapper {

    /**
     * TodoEntity をドメインの Todo に変換する。
     *
     * <p>Entity のプリミティブ値を ValueObject でラップし、 ドメインモデルとして再構成する。
     *
     * @param entity 変換対象の TodoEntity
     * @return 変換後のドメイン Todo
     */
    public static Todo toDomain(TodoEntity entity) {
        return new Todo(
                new InternalId(entity.getInternalId()),
                new PublicId(entity.getPublicId()),
                new VersionNumber(entity.getVersionNumber()),
                entity.getTitle(),
                entity.getDetail(),
                entity.isCompletedFlag(),
                entity.isDeletedFlag(),
                new DueDate(entity.getDueDate()),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    /**
     * ドメインの Todo を TodoEntity に変換する。
     *
     * <p>Domain の ValueObject から値を抽出し、 Entity のプリミティブフィールドに設定する。
     *
     * @param domain 変換対象のドメイン Todo
     * @return 変換後の TodoEntity
     */
    public static TodoEntity toEntity(Todo domain) {
        TodoEntity entity = new TodoEntity();
        // internalId は新規作成時は null のため、null セーフに設定する
        entity.setInternalId(
                domain.getInternalId() != null ? domain.getInternalId().value() : null);
        entity.setPublicId(domain.getPublicId().value());
        entity.setVersionNumber(domain.getVersionNumber().value());
        entity.setTitle(domain.getTitle());
        entity.setDetail(domain.getDetail());
        entity.setCompletedFlag(domain.isCompleted());
        entity.setDeletedFlag(domain.isDeleted());
        entity.setDueDate(domain.getDueDate().value());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
