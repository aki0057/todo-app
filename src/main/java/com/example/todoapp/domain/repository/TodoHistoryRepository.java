package com.example.todoapp.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todoapp.domain.model.value.InternalId;
import com.example.todoapp.infraatructure.entity.TodoHistoryEntity;
import com.example.todoapp.infraatructure.entity.TodoHistoryId;

@Repository
public interface TodoHistoryRepository extends JpaRepository<TodoHistoryEntity, TodoHistoryId> {
    List<TodoHistoryEntity> findByInternalId(InternalId internalId);
}
