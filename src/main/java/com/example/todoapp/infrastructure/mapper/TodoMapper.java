package com.example.todoapp.infrastructure.mapper;

import com.example.todoapp.domain.model.todo.Todo;
import com.example.todoapp.domain.model.todo.value.InternalId;
import com.example.todoapp.domain.model.todo.value.PublicId;
import com.example.todoapp.infrastructure.entity.TodoEntity;

public class TodoMapper {

    public static Todo toDomain(TodoEntity entity) {
        return new Todo(
            new InternalId(entity.getInternalId()),
            new PublicId(entity.getPublicId()),
            entity.getTitle(),
            entity.getDetail(),
            entity.isCompletedFlag(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public static TodoEntity toEntity(Todo domain) {
        TodoEntity entity = new TodoEntity();
        entity.setInternalId(domain.getInternalId().value());
        entity.setPublicId(domain.getPublicId().value());
        entity.setTitle(domain.getTitle());
        entity.setDetail(domain.getDetail());
        entity.setCompletedFlag(domain.isCompleted());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
