package com.example.todoapp.domain.repository;

import java.util.List;
import java.util.Optional;

import com.example.todoapp.domain.model.todo.Todo;
import com.example.todoapp.domain.model.todo.value.InternalId;
import com.example.todoapp.domain.model.todo.value.PublicId;

public interface TodoDomainRepository {

    Optional<Todo> findByInternalId(InternalId id);

    Optional<Todo> findByPublicId(PublicId id);

    List<Todo> findAll();

    Todo save(Todo todo);
}

