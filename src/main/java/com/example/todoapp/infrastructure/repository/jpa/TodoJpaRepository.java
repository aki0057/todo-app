package com.example.todoapp.infrastructure.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todoapp.infrastructure.entity.TodoEntity;

@Repository
public interface TodoJpaRepository extends JpaRepository<TodoEntity, Integer> {
    Optional<TodoEntity> findByInternalId(Integer internalId);
    Optional<TodoEntity> findByPublicId(String publicId);

}

