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

@Repository
public class TodoRepositoryImpl implements TodoDomainRepository {

    private final TodoJpaRepository jpa;

    public TodoRepositoryImpl(TodoJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Todo> findByInternalId(InternalId id) {
        return jpa.findByInternalId(id.value())
                  .map(TodoMapper::toDomain);
    }

    @Override
    public Optional<Todo> findByPublicId(PublicId id) {
        return jpa.findByPublicId(id.value())
                  .map(TodoMapper::toDomain);
    }

    @Override
    public List<Todo> findAll() {
        return jpa.findAll()
                  .stream()
                  .map(TodoMapper::toDomain)
                  .toList();
    }

    @Override
    public Todo save(Todo todo) {
        TodoEntity entity = TodoMapper.toEntity(todo);
        TodoEntity saved = jpa.save(entity);
        return TodoMapper.toDomain(saved);
    }
}
