package com.example.todoapp.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todoapp.domain.model.value.InternalId;
import com.example.todoapp.domain.model.value.PublicId;
import com.example.todoapp.infraatructure.entity.TodoEntity;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, InternalId> {
    Optional<TodoEntity> findByPublicId(PublicId publicId);
}

