package com.example.todoapp.infrastructure.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todoapp.infrastructure.entity.TodoHistoryEntity;
import com.example.todoapp.infrastructure.entity.TodoHistoryId;

@Repository
public interface TodoHistoryJpaRepository extends JpaRepository<TodoHistoryEntity, TodoHistoryId> {
    List<TodoHistoryEntity> findByInternalId(Integer internalId);
}
